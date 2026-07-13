package com.kodekittu.movieticketbooking.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class TimeProvider {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
