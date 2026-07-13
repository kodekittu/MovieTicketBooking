package com.kodekittu.movieticketbooking.mapper;

import com.kodekittu.movieticketbooking.dto.request.DiscountCouponRequest;
import com.kodekittu.movieticketbooking.dto.response.DiscountCouponResponse;
import com.kodekittu.movieticketbooking.entity.DiscountCoupon;
import org.springframework.stereotype.Component;

@Component
public class DiscountCouponMapper {

    public DiscountCoupon toEntity(DiscountCouponRequest request) {
        DiscountCoupon coupon = new DiscountCoupon();
        update(coupon, request);
        return coupon;
    }

    public void update(DiscountCoupon coupon, DiscountCouponRequest request) {
        coupon.setCode(request.code().toUpperCase());
        coupon.setDescription(request.description());
        coupon.setDiscountType(request.discountType());
        coupon.setDiscountValue(request.discountValue());
        coupon.setMaxDiscountAmount(request.maxDiscountAmount());
        coupon.setMinBookingAmount(request.minBookingAmount());
        coupon.setUsageLimit(request.usageLimit());
        coupon.setPerUserLimit(request.perUserLimit());
        coupon.setValidFrom(request.validFrom());
        coupon.setValidUntil(request.validUntil());
        coupon.setActive(request.active());
    }

    public DiscountCouponResponse toResponse(DiscountCoupon coupon) {
        return new DiscountCouponResponse(
                coupon.getId(),
                coupon.getCode(),
                coupon.getDescription(),
                coupon.getDiscountType(),
                coupon.getDiscountValue(),
                coupon.getMaxDiscountAmount(),
                coupon.getMinBookingAmount(),
                coupon.getUsageLimit(),
                coupon.getUsedCount(),
                coupon.getPerUserLimit(),
                coupon.getValidFrom(),
                coupon.getValidUntil(),
                coupon.isActive(),
                coupon.getCreatedAt(),
                coupon.getUpdatedAt());
    }
}
