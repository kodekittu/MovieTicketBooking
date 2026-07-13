package com.kodekittu.movieticketbooking.dto.response;

import com.kodekittu.movieticketbooking.entity.enums.SeatHoldStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record SeatHoldResponse(
        UUID id,
        UUID userId,
        UUID showId,
        SeatHoldStatus status,
        Instant expiresAt,
        List<ShowSeatResponse> seats,
        Instant createdAt,
        Instant updatedAt
) {
}
