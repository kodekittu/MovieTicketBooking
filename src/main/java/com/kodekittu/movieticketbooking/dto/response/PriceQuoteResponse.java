package com.kodekittu.movieticketbooking.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record PriceQuoteResponse(
        UUID showId,
        List<ShowSeatResponse> seats,
        BigDecimal subtotalAmount,
        BigDecimal discountAmount,
        BigDecimal taxAmount,
        BigDecimal totalAmount
) {
}
