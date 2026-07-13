package com.kodekittu.movieticketbooking.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateBookingRequest(
        @NotNull UUID seatHoldId,
        @Size(max = 50) String discountCode
) {
}
