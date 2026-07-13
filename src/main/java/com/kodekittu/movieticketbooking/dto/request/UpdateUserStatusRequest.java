package com.kodekittu.movieticketbooking.dto.request;

import com.kodekittu.movieticketbooking.entity.enums.UserStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateUserStatusRequest(
        @NotNull UserStatus status
) {
}
