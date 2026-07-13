package com.kodekittu.movieticketbooking.mapper;

import com.kodekittu.movieticketbooking.dto.request.SeatRequest;
import com.kodekittu.movieticketbooking.dto.response.SeatResponse;
import com.kodekittu.movieticketbooking.entity.Screen;
import com.kodekittu.movieticketbooking.entity.Seat;
import org.springframework.stereotype.Component;

@Component
public class SeatMapper {

    public Seat toEntity(SeatRequest request, Screen screen) {
        Seat seat = new Seat();
        seat.setScreen(screen);
        update(seat, request);
        return seat;
    }

    public void update(Seat seat, SeatRequest request) {
        seat.setRowLabel(request.rowLabel());
        seat.setSeatNumber(request.seatNumber());
        seat.setSeatType(request.seatType());
        seat.setActive(request.active());
    }

    public SeatResponse toResponse(Seat seat) {
        return new SeatResponse(
                seat.getId(),
                seat.getScreen().getId(),
                seat.getRowLabel(),
                seat.getSeatNumber(),
                seat.label(),
                seat.getSeatType(),
                seat.isActive(),
                seat.getCreatedAt(),
                seat.getUpdatedAt());
    }
}
