package com.kodekittu.movieticketbooking.service.strategy.refund;

import com.kodekittu.movieticketbooking.entity.Booking;
import com.kodekittu.movieticketbooking.entity.RefundPolicy;

import java.math.BigDecimal;

public interface RefundStrategy {

    boolean supports(RefundPolicy policy);

    BigDecimal calculate(Booking booking, RefundPolicy policy);
}
