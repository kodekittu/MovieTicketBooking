package com.kodekittu.movieticketbooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CityRequest(
        @NotBlank @Size(max = 150) String name,
        @Size(max = 150) String state,
        @NotBlank @Size(max = 150) String country,
        boolean active
) {
}
