package com.kodekittu.movieticketbooking.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record BulkSeatRequest(
        @NotEmpty List<@Valid SeatRequest> seats
) {
}
