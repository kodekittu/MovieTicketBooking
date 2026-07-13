package com.kodekittu.movieticketbooking.service.strategy.refund;

import com.kodekittu.movieticketbooking.entity.Booking;
import com.kodekittu.movieticketbooking.entity.RefundPolicy;
import com.kodekittu.movieticketbooking.util.MoneyUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class PercentageRefundStrategy implements RefundStrategy {

    @Override
    public boolean supports(RefundPolicy policy) {
        return policy.getRefundPercentage() != null;
    }

    @Override
    public BigDecimal calculate(Booking booking, RefundPolicy policy) {
        return MoneyUtils.money(booking.getTotalAmount()
                .multiply(policy.getRefundPercentage())
                .divide(MoneyUtils.ONE_HUNDRED, 2, RoundingMode.HALF_UP));
    }
}
