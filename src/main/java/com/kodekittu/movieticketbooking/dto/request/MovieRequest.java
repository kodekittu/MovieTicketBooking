package com.kodekittu.movieticketbooking.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record MovieRequest(
        @NotBlank @Size(max = 255) String title,
        String description,
        @NotBlank @Size(max = 50) String language,
        @Size(max = 100) String genre,
        @Min(1) int durationMinutes,
        @Size(max = 20) String certificate,
        @NotNull LocalDate releaseDate,
        boolean active
) {
}
