package com.kodekittu.movieticketbooking.service;

import com.kodekittu.movieticketbooking.dto.common.PageResponse;
import com.kodekittu.movieticketbooking.dto.response.NotificationResponse;
import com.kodekittu.movieticketbooking.entity.Booking;
import com.kodekittu.movieticketbooking.entity.Notification;
import com.kodekittu.movieticketbooking.entity.User;
import com.kodekittu.movieticketbooking.entity.enums.NotificationChannel;
import com.kodekittu.movieticketbooking.entity.enums.NotificationStatus;
import com.kodekittu.movieticketbooking.entity.enums.NotificationType;
import com.kodekittu.movieticketbooking.exception.ResourceNotFoundException;
import com.kodekittu.movieticketbooking.mapper.NotificationMapper;
import com.kodekittu.movieticketbooking.repository.BookingRepository;
import com.kodekittu.movieticketbooking.repository.NotificationRepository;
import com.kodekittu.movieticketbooking.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final BookingRepository bookingRepository;
    private final ReferenceDataService referenceDataService;
    private final NotificationMapper notificationMapper;
    private final SecurityUtils securityUtils;
    private final Clock clock;

    @Transactional(readOnly = true)
    public PageResponse<NotificationResponse> listCurrentUserNotifications(Pageable pageable) {
        return PageResponse.from(notificationRepository.findByUserId(securityUtils.currentUserId(), pageable)
                .map(notificationMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public NotificationResponse get(UUID id) {
        return notificationRepository.findById(id)
                .map(notificationMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
    }

    @Transactional
    public NotificationResponse retry(UUID id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        deliver(notification);
        return notificationMapper.toResponse(notification);
    }

    @Transactional
    public void createBookingConfirmation(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        createAndDeliver(booking.getUser(), booking, NotificationType.BOOKING_CONFIRMATION,
                "Booking confirmed", "Your booking " + booking.getBookingReference() + " is confirmed.");
    }

    @Transactional
    public void createCancellationConfirmation(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        createAndDeliver(booking.getUser(), booking, NotificationType.CANCELLATION_CONFIRMATION,
                "Booking cancelled", "Your booking " + booking.getBookingReference() + " has been cancelled.");
    }

    @Transactional
    public void createReminder(UUID userId, UUID bookingId) {
        User user = referenceDataService.user(userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        createAndDeliver(user, booking, NotificationType.SHOW_REMINDER,
                "Show reminder", "Your show starts soon for booking " + booking.getBookingReference() + ".");
    }

    @Transactional
    public int retryFailed(int batchSize) {
        var failedNotifications = notificationRepository.findByStatus(NotificationStatus.FAILED, PageRequest.of(0, batchSize));
        failedNotifications.forEach(this::deliver);
        return failedNotifications.size();
    }

    private void createAndDeliver(User user, Booking booking, NotificationType type, String subject, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setBooking(booking);
        notification.setType(type);
        notification.setChannel(NotificationChannel.EMAIL);
        notification.setRecipient(user.getEmail());
        notification.setSubject(subject);
        notification.setMessage(message);
        notificationRepository.save(notification);
        deliver(notification);
    }

    private void deliver(Notification notification) {
        notification.setRetryCount(notification.getRetryCount() + 1);
        notification.setStatus(NotificationStatus.SENT);
        notification.setSentAt(clock.instant());
        notification.setLastError(null);
    }
}
