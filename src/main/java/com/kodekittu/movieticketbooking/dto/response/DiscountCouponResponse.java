package com.kodekittu.movieticketbooking.dto.response;

import com.kodekittu.movieticketbooking.entity.enums.DiscountType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record DiscountCouponResponse(
        UUID id,
        String code,
        String description,
        DiscountType discountType,
        BigDecimal discountValue,
        BigDecimal maxDiscountAmount,
        BigDecimal minBookingAmount,
        Integer usageLimit,
        int usedCount,
        Integer perUserLimit,
        Instant validFrom,
        Instant validUntil,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
}
