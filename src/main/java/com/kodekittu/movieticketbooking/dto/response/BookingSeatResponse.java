package com.kodekittu.movieticketbooking.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record BookingSeatResponse(
        UUID id,
        UUID seatId,
        UUID showSeatId,
        String seatLabel,
        BigDecimal price
) {
}
