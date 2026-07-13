package com.kodekittu.movieticketbooking.dto.response;

import com.kodekittu.movieticketbooking.entity.enums.PricingRuleType;
import com.kodekittu.movieticketbooking.entity.enums.SeatType;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Instant;
import java.util.UUID;

public record PricingTierResponse(
        UUID id,
        String name,
        PricingRuleType ruleType,
        SeatType seatType,
        DayOfWeek dayOfWeek,
        UUID movieId,
        UUID theaterId,
        UUID screenId,
        UUID showId,
        BigDecimal basePrice,
        BigDecimal multiplier,
        int priority,
        boolean active,
        Instant validFrom,
        Instant validUntil,
        Instant createdAt,
        Instant updatedAt
) {
}
