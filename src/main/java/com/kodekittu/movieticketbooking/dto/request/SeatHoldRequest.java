package com.kodekittu.movieticketbooking.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record SeatHoldRequest(
        @NotNull UUID showId,
        @NotEmpty List<@NotNull UUID> seatIds
) {
}
