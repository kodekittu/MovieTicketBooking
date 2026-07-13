package com.kodekittu.movieticketbooking.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ValidateDiscountRequest(
        @NotBlank @Size(max = 50) String code,
        @NotNull @DecimalMin(value = "0.00") BigDecimal amount
) {
}
