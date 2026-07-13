package com.kodekittu.movieticketbooking.service;

import com.kodekittu.movieticketbooking.dto.common.PageResponse;
import com.kodekittu.movieticketbooking.dto.request.DiscountCouponRequest;
import com.kodekittu.movieticketbooking.dto.request.ValidateDiscountRequest;
import com.kodekittu.movieticketbooking.dto.response.DiscountCouponResponse;
import com.kodekittu.movieticketbooking.dto.response.DiscountValidationResponse;
import com.kodekittu.movieticketbooking.entity.DiscountCoupon;
import com.kodekittu.movieticketbooking.exception.BusinessException;
import com.kodekittu.movieticketbooking.exception.ErrorCode;
import com.kodekittu.movieticketbooking.exception.ResourceNotFoundException;
import com.kodekittu.movieticketbooking.mapper.DiscountCouponMapper;
import com.kodekittu.movieticketbooking.repository.DiscountCouponRepository;
import com.kodekittu.movieticketbooking.service.strategy.discount.DiscountStrategy;
import com.kodekittu.movieticketbooking.util.MoneyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountCouponRepository discountCouponRepository;
    private final DiscountCouponMapper discountCouponMapper;
    private final List<DiscountStrategy> discountStrategies;
    private final Clock clock;

    @Transactional
    public DiscountCouponResponse create(DiscountCouponRequest request) {
        validateValidityWindow(request.validFrom(), request.validUntil());
        return discountCouponMapper.toResponse(discountCouponRepository.save(discountCouponMapper.toEntity(request)));
    }

    @Transactional(readOnly = true)
    public PageResponse<DiscountCouponResponse> list(Pageable pageable) {
        return PageResponse.from(discountCouponRepository.findAll(pageable).map(discountCouponMapper::toResponse));
    }

    @Transactional
    public DiscountCouponResponse update(java.util.UUID id, DiscountCouponRequest request) {
        validateValidityWindow(request.validFrom(), request.validUntil());
        DiscountCoupon coupon = discountCouponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discount coupon not found"));
        discountCouponMapper.update(coupon, request);
        return discountCouponMapper.toResponse(coupon);
    }

    @Transactional
    public void delete(java.util.UUID id) {
        DiscountCoupon coupon = discountCouponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discount coupon not found"));
        coupon.setActive(false);
    }

    @Transactional(readOnly = true)
    public DiscountValidationResponse validate(ValidateDiscountRequest request) {
        return discountCouponRepository.findByCodeIgnoreCase(request.code())
                .map(coupon -> {
                    BigDecimal amount = calculateDiscount(request.amount(), coupon);
                    return new DiscountValidationResponse(coupon.getCode(), amount.compareTo(BigDecimal.ZERO) > 0, amount, "Coupon is valid");
                })
                .orElseGet(() -> new DiscountValidationResponse(request.code(), false, MoneyUtils.ZERO, "Coupon not found"));
    }

    public BigDecimal calculateDiscount(BigDecimal subtotal, String code) {
        if (!org.springframework.util.StringUtils.hasText(code)) {
            return MoneyUtils.ZERO;
        }
        DiscountCoupon coupon = discountCouponRepository.findByCodeIgnoreCase(code)
                .orElseThrow(() -> new BusinessException(ErrorCode.VALIDATION_FAILED, HttpStatus.BAD_REQUEST, "Invalid discount code"));
        return calculateDiscount(subtotal, coupon);
    }

    private BigDecimal calculateDiscount(BigDecimal subtotal, DiscountCoupon coupon) {
        validateCoupon(coupon, subtotal);
        DiscountStrategy strategy = discountStrategies.stream()
                .filter(candidate -> candidate.supports(coupon))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.BUSINESS_RULE_VIOLATION, HttpStatus.UNPROCESSABLE_ENTITY,
                        "Unsupported discount type"));
        return strategy.calculate(subtotal, coupon);
    }

    private void validateCoupon(DiscountCoupon coupon, BigDecimal subtotal) {
        Instant now = clock.instant();
        if (!coupon.isActive() || now.isBefore(coupon.getValidFrom()) || now.isAfter(coupon.getValidUntil())) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED, HttpStatus.BAD_REQUEST, "Discount coupon is not active");
        }
        if (coupon.getUsageLimit() != null && coupon.getUsedCount() >= coupon.getUsageLimit()) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED, HttpStatus.BAD_REQUEST, "Discount coupon usage limit reached");
        }
        if (coupon.getMinBookingAmount() != null && subtotal.compareTo(coupon.getMinBookingAmount()) < 0) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED, HttpStatus.BAD_REQUEST, "Booking amount is below coupon minimum");
        }
    }

    private void validateValidityWindow(Instant from, Instant until) {
        if (!until.isAfter(from)) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED, HttpStatus.BAD_REQUEST, "Coupon validUntil must be after validFrom");
        }
    }
}
