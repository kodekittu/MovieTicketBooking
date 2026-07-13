package com.kodekittu.movieticketbooking.controller;

import com.kodekittu.movieticketbooking.dto.common.PageResponse;
import com.kodekittu.movieticketbooking.dto.response.NotificationResponse;
import com.kodekittu.movieticketbooking.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public PageResponse<NotificationResponse> list(Pageable pageable) {
        return notificationService.listCurrentUserNotifications(pageable);
    }

    @GetMapping("/{id}")
    public NotificationResponse get(@PathVariable UUID id) {
        return notificationService.get(id);
    }

    @PostMapping("/{id}/retry")
    @PreAuthorize("hasRole('ADMIN')")
    public NotificationResponse retry(@PathVariable UUID id) {
        return notificationService.retry(id);
    }
}
