package com.kodekittu.movieticketbooking.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.format.DateTimeFormatter;

@Component
public class BookingReferenceGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd")
            .withZone(java.time.ZoneOffset.UTC);

    private final Clock clock;

    public BookingReferenceGenerator(Clock clock) {
        this.clock = clock;
    }

    public String nextReference() {
        int suffix = RANDOM.nextInt(100_000, 1_000_000);
        return "MTB-" + DATE_FORMATTER.format(clock.instant()) + "-" + suffix;
    }
}
