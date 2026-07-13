package com.kodekittu.movieticketbooking.service;

import com.kodekittu.movieticketbooking.dto.common.PageResponse;
import com.kodekittu.movieticketbooking.dto.request.PriceQuoteRequest;
import com.kodekittu.movieticketbooking.dto.request.PricingTierRequest;
import com.kodekittu.movieticketbooking.dto.response.PriceQuoteResponse;
import com.kodekittu.movieticketbooking.dto.response.PricingTierResponse;
import com.kodekittu.movieticketbooking.entity.Movie;
import com.kodekittu.movieticketbooking.entity.PricingTier;
import com.kodekittu.movieticketbooking.entity.Screen;
import com.kodekittu.movieticketbooking.entity.Show;
import com.kodekittu.movieticketbooking.entity.ShowSeat;
import com.kodekittu.movieticketbooking.entity.Theater;
import com.kodekittu.movieticketbooking.exception.BusinessException;
import com.kodekittu.movieticketbooking.exception.ErrorCode;
import com.kodekittu.movieticketbooking.exception.ResourceNotFoundException;
import com.kodekittu.movieticketbooking.mapper.PricingTierMapper;
import com.kodekittu.movieticketbooking.mapper.ShowSeatMapper;
import com.kodekittu.movieticketbooking.repository.PricingTierRepository;
import com.kodekittu.movieticketbooking.repository.ShowSeatRepository;
import com.kodekittu.movieticketbooking.service.strategy.pricing.PricingStrategy;
import com.kodekittu.movieticketbooking.util.MoneyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PricingService {

    private static final BigDecimal DEFAULT_BASE_PRICE = new BigDecimal("200.00");

    private final PricingTierRepository pricingTierRepository;
    private final ShowSeatRepository showSeatRepository;
    private final ReferenceDataService referenceDataService;
    private final DiscountService discountService;
    private final PricingTierMapper pricingTierMapper;
    private final ShowSeatMapper showSeatMapper;
    private final List<PricingStrategy> pricingStrategies;
    private final Clock clock;

    @Transactional
    public PricingTierResponse create(PricingTierRequest request) {
        return pricingTierMapper.toResponse(pricingTierRepository.save(toTier(request)));
    }

    @Transactional(readOnly = true)
    public PageResponse<PricingTierResponse> list(Pageable pageable) {
        return PageResponse.from(pricingTierRepository.findAll(pageable).map(pricingTierMapper::toResponse));
    }

    @Transactional
    public PricingTierResponse update(UUID id, PricingTierRequest request) {
        PricingTier tier = pricingTierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pricing tier not found"));
        applyTierUpdate(tier, request);
        return pricingTierMapper.toResponse(tier);
    }

    @Transactional
    public void delete(UUID id) {
        PricingTier tier = pricingTierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pricing tier not found"));
        tier.setActive(false);
    }

    @Transactional(readOnly = true)
    public PriceQuoteResponse quote(PriceQuoteRequest request) {
        List<ShowSeat> seats = showSeatRepository.findByShowIdAndSeatIdIn(request.showId(), request.seatIds());
        PricingResult result = calculate(request.showId(), request.seatIds(), request.discountCode(), seats);
        return new PriceQuoteResponse(
                request.showId(),
                seats.stream().map(showSeatMapper::toResponse).toList(),
                result.subtotalAmount(),
                result.discountAmount(),
                result.taxAmount(),
                result.totalAmount());
    }

    public PricingResult calculate(UUID showId, Collection<UUID> seatIds, String discountCode, List<ShowSeat> selectedSeats) {
        if (selectedSeats.size() != seatIds.size()) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED, HttpStatus.BAD_REQUEST, "One or more seats do not belong to this show");
        }
        if (selectedSeats.isEmpty()) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED, HttpStatus.BAD_REQUEST, "At least one seat must be selected");
        }
        Show show = selectedSeats.getFirst().getShow();
        List<PricingTier> tiers = pricingTierRepository.findApplicablePricingTiers(
                show.getMovie().getId(),
                show.getScreen().getTheater().getId(),
                show.getScreen().getId(),
                showId,
                clock.instant());
        Map<UUID, BigDecimal> seatPrices = new LinkedHashMap<>();
        BigDecimal subtotal = MoneyUtils.ZERO;
        for (ShowSeat seat : selectedSeats) {
            BigDecimal price = priceSeat(seat, tiers);
            seatPrices.put(seat.getSeat().getId(), price);
            subtotal = subtotal.add(price);
        }
        subtotal = MoneyUtils.money(subtotal);
        BigDecimal discount = discountService.calculateDiscount(subtotal, discountCode);
        BigDecimal taxableAmount = subtotal.subtract(discount).max(BigDecimal.ZERO);
        BigDecimal tax = MoneyUtils.money(taxableAmount.multiply(MoneyUtils.TAX_RATE));
        return new PricingResult(seatPrices, subtotal, discount, tax, MoneyUtils.money(taxableAmount.add(tax)));
    }

    private BigDecimal priceSeat(ShowSeat showSeat, List<PricingTier> tiers) {
        BigDecimal price = showSeat.getPrice().compareTo(BigDecimal.ZERO) > 0 ? showSeat.getPrice() : DEFAULT_BASE_PRICE;
        for (PricingTier tier : tiers) {
            for (PricingStrategy strategy : pricingStrategies) {
                if (strategy.supports(tier, showSeat)) {
                    price = strategy.apply(price, tier, showSeat);
                    break;
                }
            }
        }
        return MoneyUtils.money(price);
    }

    private PricingTier toTier(PricingTierRequest request) {
        return pricingTierMapper.toEntity(
                request,
                request.movieId() == null ? null : referenceDataService.movie(request.movieId()),
                request.theaterId() == null ? null : referenceDataService.theater(request.theaterId()),
                request.screenId() == null ? null : referenceDataService.screen(request.screenId()),
                request.showId() == null ? null : referenceDataService.show(request.showId()));
    }

    private void applyTierUpdate(PricingTier tier, PricingTierRequest request) {
        Movie movie = request.movieId() == null ? null : referenceDataService.movie(request.movieId());
        Theater theater = request.theaterId() == null ? null : referenceDataService.theater(request.theaterId());
        Screen screen = request.screenId() == null ? null : referenceDataService.screen(request.screenId());
        Show show = request.showId() == null ? null : referenceDataService.show(request.showId());
        pricingTierMapper.update(tier, request, movie, theater, screen, show);
    }
}
