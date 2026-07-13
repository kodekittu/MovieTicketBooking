package com.kodekittu.movieticketbooking.mapper;

import com.kodekittu.movieticketbooking.dto.response.BookingResponse;
import com.kodekittu.movieticketbooking.dto.response.BookingSeatResponse;
import com.kodekittu.movieticketbooking.entity.Booking;
import com.kodekittu.movieticketbooking.entity.BookingSeat;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingMapper {

    public BookingSeatResponse toSeatResponse(BookingSeat bookingSeat) {
        return new BookingSeatResponse(
                bookingSeat.getId(),
                bookingSeat.getSeat().getId(),
                bookingSeat.getShowSeat().getId(),
                bookingSeat.getSeatLabel(),
                bookingSeat.getPrice());
    }

    public BookingResponse toResponse(Booking booking, List<BookingSeat> seats) {
        return new BookingResponse(
                booking.getId(),
                booking.getBookingReference(),
                booking.getUser().getId(),
                booking.getShow().getId(),
                booking.getStatus(),
                booking.getSubtotalAmount(),
                booking.getDiscountAmount(),
                booking.getTaxAmount(),
                booking.getTotalAmount(),
                seats.stream().map(this::toSeatResponse).toList(),
                booking.getCancelledAt(),
                booking.getCreatedAt(),
                booking.getUpdatedAt());
    }
}
