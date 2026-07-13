package com.kodekittu.movieticketbooking.listener;

import com.kodekittu.movieticketbooking.ai.service.AIRecommendationService;
import com.kodekittu.movieticketbooking.entity.Booking;
import com.kodekittu.movieticketbooking.event.BookingConfirmedEvent;
import com.kodekittu.movieticketbooking.exception.ResourceNotFoundException;
import com.kodekittu.movieticketbooking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AIRecommendationListener {

    private final BookingRepository bookingRepository;
    private final AIRecommendationService recommendationService;

    @Async
    @EventListener
    public void handleBookingConfirmed(BookingConfirmedEvent event) {

        Booking booking = bookingRepository.findWithDetailsById(event.bookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        recommendationService.generateRecommendation(booking.getUser());
    }

}