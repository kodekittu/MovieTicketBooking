package com.kodekittu.movieticketbooking.mapper;

import com.kodekittu.movieticketbooking.dto.response.ShowSeatResponse;
import com.kodekittu.movieticketbooking.entity.Seat;
import com.kodekittu.movieticketbooking.entity.ShowSeat;
import org.springframework.stereotype.Component;

@Component
public class ShowSeatMapper {

    public ShowSeatResponse toResponse(ShowSeat showSeat) {
        Seat seat = showSeat.getSeat();
        return new ShowSeatResponse(
                showSeat.getId(),
                showSeat.getShow().getId(),
                seat.getId(),
                seat.getRowLabel(),
                seat.getSeatNumber(),
                seat.label(),
                seat.getSeatType(),
                showSeat.getStatus(),
                showSeat.getPrice());
    }
}
