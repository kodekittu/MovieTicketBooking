package com.kodekittu.movieticketbooking.dto.request;

import com.kodekittu.movieticketbooking.entity.enums.RefundScope;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record RefundPolicyRequest(
        @NotBlank @Size(max = 150) String name,
        @NotNull RefundScope scope,
        UUID movieId,
        UUID theaterId,
        @Min(0) int hoursBeforeShow,
        @NotNull @DecimalMin("0.00") @DecimalMax("100.00") BigDecimal refundPercentage,
        int priority,
        boolean active
) {
}
