package com.kodekittu.movieticketbooking.dto.request;

import com.kodekittu.movieticketbooking.entity.enums.PricingRuleType;
import com.kodekittu.movieticketbooking.entity.enums.SeatType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Instant;
import java.util.UUID;

public record PricingTierRequest(
        @NotBlank @Size(max = 150) String name,
        @NotNull PricingRuleType ruleType,
        SeatType seatType,
        DayOfWeek dayOfWeek,
        UUID movieId,
        UUID theaterId,
        UUID screenId,
        UUID showId,
        @DecimalMin(value = "0.00") BigDecimal basePrice,
        @DecimalMin(value = "0.00") BigDecimal multiplier,
        int priority,
        boolean active,
        Instant validFrom,
        Instant validUntil
) {
}
