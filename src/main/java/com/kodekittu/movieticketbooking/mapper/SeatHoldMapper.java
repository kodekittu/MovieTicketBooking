package com.kodekittu.movieticketbooking.mapper;

import com.kodekittu.movieticketbooking.dto.response.SeatHoldResponse;
import com.kodekittu.movieticketbooking.dto.response.ShowSeatResponse;
import com.kodekittu.movieticketbooking.entity.SeatHold;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SeatHoldMapper {

    private final ShowSeatMapper showSeatMapper;

    public SeatHoldResponse toResponse(SeatHold seatHold, List<ShowSeatResponse> seats) {
        return new SeatHoldResponse(
                seatHold.getId(),
                seatHold.getUser().getId(),
                seatHold.getShow().getId(),
                seatHold.getStatus(),
                seatHold.getExpiresAt(),
                seats,
                seatHold.getCreatedAt(),
                seatHold.getUpdatedAt());
    }

    public SeatHoldResponse toResponse(SeatHold seatHold, List<com.kodekittu.movieticketbooking.entity.ShowSeat> seats, boolean mapSeats) {
        List<ShowSeatResponse> mappedSeats = mapSeats
                ? seats.stream().map(showSeatMapper::toResponse).toList()
                : List.of();
        return toResponse(seatHold, mappedSeats);
    }
}
