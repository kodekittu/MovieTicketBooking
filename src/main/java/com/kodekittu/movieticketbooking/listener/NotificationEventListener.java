package com.kodekittu.movieticketbooking.listener;

import com.kodekittu.movieticketbooking.event.BookingCancelledEvent;
import com.kodekittu.movieticketbooking.event.BookingConfirmedEvent;
import com.kodekittu.movieticketbooking.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationService notificationService;

    @Async("applicationTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onBookingConfirmed(BookingConfirmedEvent event) {
        notificationService.createBookingConfirmation(event.bookingId());
    }

    @Async("applicationTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onBookingCancelled(BookingCancelledEvent event) {
        notificationService.createCancellationConfirmation(event.bookingId());
    }
}
