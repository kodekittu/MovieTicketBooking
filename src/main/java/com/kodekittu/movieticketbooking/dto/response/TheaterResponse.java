package com.kodekittu.movieticketbooking.dto.response;

import java.time.Instant;
import java.util.UUID;

public record TheaterResponse(
        UUID id,
        UUID cityId,
        String cityName,
        String name,
        String address,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
}
