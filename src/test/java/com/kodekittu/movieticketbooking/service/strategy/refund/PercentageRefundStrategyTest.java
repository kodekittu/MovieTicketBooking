package com.kodekittu.movieticketbooking.service.strategy.refund;

import com.kodekittu.movieticketbooking.entity.Booking;
import com.kodekittu.movieticketbooking.entity.RefundPolicy;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PercentageRefundStrategyTest {

    private final PercentageRefundStrategy strategy = new PercentageRefundStrategy();

    @Test
    void calculatesConfiguredRefundPercentage() {
        Booking booking = new Booking();
        booking.setTotalAmount(new BigDecimal("500.00"));
        RefundPolicy policy = new RefundPolicy();
        policy.setRefundPercentage(new BigDecimal("70.00"));

        BigDecimal refund = strategy.calculate(booking, policy);

        assertThat(refund).isEqualByComparingTo("350.00");
    }
}
