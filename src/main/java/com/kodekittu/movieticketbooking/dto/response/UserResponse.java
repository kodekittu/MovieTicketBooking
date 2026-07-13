package com.kodekittu.movieticketbooking.dto.response;

import com.kodekittu.movieticketbooking.entity.enums.UserStatus;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        String phone,
        UserStatus status,
        Set<String> roles,
        Instant createdAt,
        Instant updatedAt
) {
}
