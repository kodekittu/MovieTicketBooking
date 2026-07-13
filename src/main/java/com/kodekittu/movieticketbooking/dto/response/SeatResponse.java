package com.kodekittu.movieticketbooking.dto.response;

import com.kodekittu.movieticketbooking.entity.enums.SeatType;

import java.time.Instant;
import java.util.UUID;

public record SeatResponse(
        UUID id,
        UUID screenId,
        String rowLabel,
        int seatNumber,
        String label,
        SeatType seatType,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
}
