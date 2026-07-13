package com.kodekittu.movieticketbooking.event;

import java.util.UUID;

public record PaymentFailedEvent(UUID bookingId, String reason) {
}
