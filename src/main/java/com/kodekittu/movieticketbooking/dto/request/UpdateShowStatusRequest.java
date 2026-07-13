package com.kodekittu.movieticketbooking.dto.request;

import com.kodekittu.movieticketbooking.entity.enums.ShowStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateShowStatusRequest(
        @NotNull ShowStatus status
) {
}
