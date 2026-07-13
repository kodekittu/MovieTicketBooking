package com.kodekittu.movieticketbooking.event;

import java.util.UUID;

public record BookingConfirmedEvent(UUID bookingId) {
}
