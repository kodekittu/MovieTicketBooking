package com.kodekittu.movieticketbooking.dto.response;

import com.kodekittu.movieticketbooking.entity.enums.RefundScope;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record RefundPolicyResponse(
        UUID id,
        String name,
        RefundScope scope,
        UUID movieId,
        UUID theaterId,
        int hoursBeforeShow,
        BigDecimal refundPercentage,
        int priority,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
}
