package com.kodekittu.movieticketbooking.dto.request;

import com.kodekittu.movieticketbooking.entity.enums.SeatType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SeatRequest(
        @NotBlank @Size(max = 10) String rowLabel,
        @Min(1) int seatNumber,
        @NotNull SeatType seatType,
        boolean active
) {
}
