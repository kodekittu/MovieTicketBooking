package com.kodekittu.movieticketbooking.dto.response;

import java.time.Instant;
import java.util.UUID;

public record ScreenResponse(
        UUID id,
        UUID theaterId,
        String theaterName,
        String name,
        String screenType,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
}
