package com.kodekittu.movieticketbooking.service.strategy.discount;

import com.kodekittu.movieticketbooking.entity.DiscountCoupon;

import java.math.BigDecimal;

public interface DiscountStrategy {

    boolean supports(DiscountCoupon coupon);

    BigDecimal calculate(BigDecimal amount, DiscountCoupon coupon);
}
