package com.kodekittu.movieticketbooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ScreenRequest(
        @NotNull UUID theaterId,
        @NotBlank @Size(max = 100) String name,
        @Size(max = 50) String screenType,
        boolean active
) {
}
