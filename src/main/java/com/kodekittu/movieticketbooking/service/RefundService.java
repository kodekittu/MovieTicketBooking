package com.kodekittu.movieticketbooking.service;

import com.kodekittu.movieticketbooking.dto.common.PageResponse;
import com.kodekittu.movieticketbooking.dto.request.RefundPolicyRequest;
import com.kodekittu.movieticketbooking.dto.response.RefundPolicyResponse;
import com.kodekittu.movieticketbooking.entity.Booking;
import com.kodekittu.movieticketbooking.entity.Movie;
import com.kodekittu.movieticketbooking.entity.RefundPolicy;
import com.kodekittu.movieticketbooking.entity.Theater;
import com.kodekittu.movieticketbooking.exception.ResourceNotFoundException;
import com.kodekittu.movieticketbooking.mapper.RefundPolicyMapper;
import com.kodekittu.movieticketbooking.repository.RefundPolicyRepository;
import com.kodekittu.movieticketbooking.service.strategy.refund.RefundStrategy;
import com.kodekittu.movieticketbooking.util.MoneyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final RefundPolicyRepository refundPolicyRepository;
    private final ReferenceDataService referenceDataService;
    private final RefundPolicyMapper refundPolicyMapper;
    private final List<RefundStrategy> refundStrategies;
    private final Clock clock;

    @Transactional
    public RefundPolicyResponse create(RefundPolicyRequest request) {
        return refundPolicyMapper.toResponse(refundPolicyRepository.save(toPolicy(request)));
    }

    @Transactional(readOnly = true)
    public PageResponse<RefundPolicyResponse> list(Pageable pageable) {
        return PageResponse.from(refundPolicyRepository.findAll(pageable).map(refundPolicyMapper::toResponse));
    }

    @Transactional
    public RefundPolicyResponse update(UUID id, RefundPolicyRequest request) {
        RefundPolicy policy = refundPolicyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Refund policy not found"));
        applyUpdate(policy, request);
        return refundPolicyMapper.toResponse(policy);
    }

    @Transactional
    public void delete(UUID id) {
        RefundPolicy policy = refundPolicyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Refund policy not found"));
        policy.setActive(false);
    }

    public BigDecimal calculateRefund(Booking booking) {
        long hoursBeforeShow = Math.max(0, Duration.between(clock.instant(), booking.getShow().getStartTime()).toHours());
        UUID movieId = booking.getShow().getMovie().getId();
        UUID theaterId = booking.getShow().getScreen().getTheater().getId();
        return refundPolicyRepository.findApplicablePolicies(movieId, theaterId, hoursBeforeShow).stream()
                .findFirst()
                .flatMap(policy -> refundStrategies.stream()
                        .filter(strategy -> strategy.supports(policy))
                        .findFirst()
                        .map(strategy -> strategy.calculate(booking, policy)))
                .orElse(MoneyUtils.ZERO);
    }

    private RefundPolicy toPolicy(RefundPolicyRequest request) {
        return refundPolicyMapper.toEntity(
                request,
                request.movieId() == null ? null : referenceDataService.movie(request.movieId()),
                request.theaterId() == null ? null : referenceDataService.theater(request.theaterId()));
    }

    private void applyUpdate(RefundPolicy policy, RefundPolicyRequest request) {
        Movie movie = request.movieId() == null ? null : referenceDataService.movie(request.movieId());
        Theater theater = request.theaterId() == null ? null : referenceDataService.theater(request.theaterId());
        refundPolicyMapper.update(policy, request, movie, theater);
    }
}
