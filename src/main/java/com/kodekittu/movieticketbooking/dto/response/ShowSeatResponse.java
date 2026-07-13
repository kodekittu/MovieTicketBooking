package com.kodekittu.movieticketbooking.dto.response;

import com.kodekittu.movieticketbooking.entity.enums.SeatStatus;
import com.kodekittu.movieticketbooking.entity.enums.SeatType;

import java.math.BigDecimal;
import java.util.UUID;

public record ShowSeatResponse(
        UUID id,
        UUID showId,
        UUID seatId,
        String rowLabel,
        int seatNumber,
        String label,
        SeatType seatType,
        SeatStatus status,
        BigDecimal price
) {
}
