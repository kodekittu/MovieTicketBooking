package com.kodekittu.movieticketbooking.controller;

import com.kodekittu.movieticketbooking.dto.common.PageResponse;
import com.kodekittu.movieticketbooking.dto.request.CancelBookingRequest;
import com.kodekittu.movieticketbooking.dto.request.CreateBookingRequest;
import com.kodekittu.movieticketbooking.dto.response.BookingResponse;
import com.kodekittu.movieticketbooking.dto.response.CancellationResponse;
import com.kodekittu.movieticketbooking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/bookings")
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse create(@Valid @RequestBody CreateBookingRequest request) {
        return bookingService.createBooking(request);
    }

    @GetMapping("/bookings/{id}")
    public BookingResponse get(@PathVariable UUID id) {
        return bookingService.getBooking(id);
    }

    @GetMapping("/bookings")
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<BookingResponse> listAll(Pageable pageable) {
        return bookingService.listBookings(pageable);
    }

    @GetMapping("/users/me/bookings")
    public PageResponse<BookingResponse> myBookings(Pageable pageable) {
        return bookingService.listMyBookings(pageable);
    }

    @PostMapping("/bookings/{id}/cancel")
    public CancellationResponse cancel(@PathVariable UUID id, @Valid @RequestBody(required = false) CancelBookingRequest request) {
        return bookingService.cancelBooking(id, request == null ? new CancelBookingRequest(null) : request);
    }
}
