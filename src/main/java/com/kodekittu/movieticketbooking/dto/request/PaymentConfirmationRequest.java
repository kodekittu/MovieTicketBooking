package com.kodekittu.movieticketbooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PaymentConfirmationRequest(
        @NotBlank @Size(max = 255) String providerReference
) {
}
