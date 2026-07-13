package com.kodekittu.movieticketbooking.dto.response;

import com.kodekittu.movieticketbooking.entity.enums.NotificationChannel;
import com.kodekittu.movieticketbooking.entity.enums.NotificationStatus;
import com.kodekittu.movieticketbooking.entity.enums.NotificationType;

import java.time.Instant;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        UUID userId,
        UUID bookingId,
        NotificationType type,
        NotificationChannel channel,
        String recipient,
        String subject,
        String message,
        NotificationStatus status,
        int retryCount,
        String lastError,
        Instant sentAt,
        Instant createdAt,
        Instant updatedAt
) {
}
