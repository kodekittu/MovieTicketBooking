package com.kodekittu.movieticketbooking.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(max = 150) String name,
        @Email @NotBlank @Size(max = 255) String email,
        @NotBlank @Size(min = 8, max = 100) String password,
        @Pattern(regexp = "^[0-9+\\-() ]{7,30}$", message = "phone must be a valid phone number")
        String phone
) {
}
