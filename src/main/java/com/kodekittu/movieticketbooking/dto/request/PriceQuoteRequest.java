package com.kodekittu.movieticketbooking.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record PriceQuoteRequest(
        @NotNull UUID showId,
        @NotEmpty List<@NotNull UUID> seatIds,
        @Size(max = 50) String discountCode
) {
}
