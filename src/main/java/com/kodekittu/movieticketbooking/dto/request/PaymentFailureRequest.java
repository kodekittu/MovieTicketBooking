package com.kodekittu.movieticketbooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PaymentFailureRequest(
        @NotBlank @Size(max = 1000) String failureReason
) {
}
