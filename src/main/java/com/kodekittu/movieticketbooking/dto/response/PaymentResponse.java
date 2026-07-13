package com.kodekittu.movieticketbooking.dto.response;

import com.kodekittu.movieticketbooking.entity.enums.PaymentMethod;
import com.kodekittu.movieticketbooking.entity.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentResponse(
        UUID id,
        UUID bookingId,
        String providerReference,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        PaymentStatus status,
        String failureReason,
        Instant createdAt,
        Instant updatedAt
) {
}
