package com.kodekittu.movieticketbooking.dto.request;

import com.kodekittu.movieticketbooking.entity.enums.DiscountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.Instant;

public record DiscountCouponRequest(
        @NotBlank @Size(max = 50) String code,
        String description,
        @NotNull DiscountType discountType,
        @NotNull @DecimalMin(value = "0.00") BigDecimal discountValue,
        @DecimalMin(value = "0.00") BigDecimal maxDiscountAmount,
        @DecimalMin(value = "0.00") BigDecimal minBookingAmount,
        Integer usageLimit,
        Integer perUserLimit,
        @NotNull Instant validFrom,
        @NotNull Instant validUntil,
        boolean active
) {
}
