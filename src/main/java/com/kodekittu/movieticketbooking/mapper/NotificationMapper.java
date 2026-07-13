package com.kodekittu.movieticketbooking.mapper;

import com.kodekittu.movieticketbooking.dto.response.NotificationResponse;
import com.kodekittu.movieticketbooking.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getUser().getId(),
                notification.getBooking() == null ? null : notification.getBooking().getId(),
                notification.getType(),
                notification.getChannel(),
                notification.getRecipient(),
                notification.getSubject(),
                notification.getMessage(),
                notification.getStatus(),
                notification.getRetryCount(),
                notification.getLastError(),
                notification.getSentAt(),
                notification.getCreatedAt(),
                notification.getUpdatedAt());
    }
}
