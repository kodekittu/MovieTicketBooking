package com.kodekittu.movieticketbooking.dto.request;

import com.kodekittu.movieticketbooking.entity.enums.ShowStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record ShowRequest(
        @NotNull UUID movieId,
        @NotNull UUID screenId,
        @NotNull @Future Instant startTime,
        @NotNull @Future Instant endTime,
        ShowStatus status
) {
}
