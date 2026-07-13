package com.kodekittu.movieticketbooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record TheaterRequest(
        @NotNull UUID cityId,
        @NotBlank @Size(max = 255) String name,
        @NotBlank String address,
        boolean active
) {
}
