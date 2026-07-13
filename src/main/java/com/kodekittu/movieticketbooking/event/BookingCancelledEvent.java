package com.kodekittu.movieticketbooking.event;

import java.math.BigDecimal;
import java.util.UUID;

public record BookingCancelledEvent(UUID bookingId, BigDecimal refundAmount) {
}
