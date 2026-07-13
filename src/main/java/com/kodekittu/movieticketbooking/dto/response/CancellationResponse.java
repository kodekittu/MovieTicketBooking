package com.kodekittu.movieticketbooking.dto.response;

import com.kodekittu.movieticketbooking.entity.enums.BookingStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record CancellationResponse(
        UUID bookingId,
        String bookingReference,
        BookingStatus status,
        BigDecimal refundAmount,
        String message
) {
}
