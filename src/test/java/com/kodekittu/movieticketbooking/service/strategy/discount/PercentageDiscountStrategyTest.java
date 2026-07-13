package com.kodekittu.movieticketbooking.service.strategy.discount;

import com.kodekittu.movieticketbooking.entity.DiscountCoupon;
import com.kodekittu.movieticketbooking.entity.enums.DiscountType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PercentageDiscountStrategyTest {

    private final PercentageDiscountStrategy strategy = new PercentageDiscountStrategy();

    @Test
    void calculatesPercentageDiscountWithCap() {
        DiscountCoupon coupon = new DiscountCoupon();
        coupon.setDiscountType(DiscountType.PERCENTAGE);
        coupon.setDiscountValue(new BigDecimal("25.00"));
        coupon.setMaxDiscountAmount(new BigDecimal("100.00"));

        BigDecimal discount = strategy.calculate(new BigDecimal("1000.00"), coupon);

        assertThat(discount).isEqualByComparingTo("100.00");
    }
}
