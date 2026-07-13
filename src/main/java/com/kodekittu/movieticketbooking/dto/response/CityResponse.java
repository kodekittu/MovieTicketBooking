package com.kodekittu.movieticketbooking.dto.response;

import java.time.Instant;
import java.util.UUID;

public record CityResponse(
        UUID id,
        String name,
        String state,
        String country,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
}
