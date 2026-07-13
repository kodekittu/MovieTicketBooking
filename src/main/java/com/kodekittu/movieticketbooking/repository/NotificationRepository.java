package com.kodekittu.movieticketbooking.repository;

import com.kodekittu.movieticketbooking.entity.Notification;
import com.kodekittu.movieticketbooking.entity.enums.NotificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    Page<Notification> findByUserId(UUID userId, Pageable pageable);

    List<Notification> findByStatus(NotificationStatus status, Pageable pageable);
}
