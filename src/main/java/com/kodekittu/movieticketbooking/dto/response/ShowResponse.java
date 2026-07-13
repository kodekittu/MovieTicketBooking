package com.kodekittu.movieticketbooking.dto.response;

import com.kodekittu.movieticketbooking.entity.enums.ShowStatus;

import java.time.Instant;
import java.util.UUID;

public record ShowResponse(
        UUID id,
        UUID movieId,
        String movieTitle,
        UUID screenId,
        String screenName,
        UUID theaterId,
        String theaterName,
        UUID cityId,
        String cityName,
        Instant startTime,
        Instant endTime,
        ShowStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
