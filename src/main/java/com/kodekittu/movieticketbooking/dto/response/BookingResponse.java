package com.kodekittu.movieticketbooking.dto.response;

import com.kodekittu.movieticketbooking.entity.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record BookingResponse(
        UUID id,
        String bookingReference,
        UUID userId,
        UUID showId,
        BookingStatus status,
        BigDecimal subtotalAmount,
        BigDecimal discountAmount,
        BigDecimal taxAmount,
        BigDecimal totalAmount,
        List<BookingSeatResponse> seats,
        Instant cancelledAt,
        Instant createdAt,
        Instant updatedAt
) {
}
