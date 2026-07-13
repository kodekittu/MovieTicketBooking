package com.kodekittu.movieticketbooking.service;

import com.kodekittu.movieticketbooking.entity.ShowSeat;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public record PricingResult(
        Map<UUID, BigDecimal> seatPrices,
        BigDecimal subtotalAmount,
        BigDecimal discountAmount,
        BigDecimal taxAmount,
        BigDecimal totalAmount
) {
    public BigDecimal priceFor(ShowSeat showSeat) {
        return seatPrices.get(showSeat.getSeat().getId());
    }
}
