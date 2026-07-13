package com.kodekittu.movieticketbooking.dto.response;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record MovieResponse(
        UUID id,
        String title,
        String description,
        String language,
        String genre,
        int durationMinutes,
        String certificate,
        LocalDate releaseDate,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
}
