package com.kodekittu.movieticketbooking.service.strategy.discount;

import com.kodekittu.movieticketbooking.entity.DiscountCoupon;
import com.kodekittu.movieticketbooking.entity.enums.DiscountType;
import com.kodekittu.movieticketbooking.util.MoneyUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class PercentageDiscountStrategy implements DiscountStrategy {

    @Override
    public boolean supports(DiscountCoupon coupon) {
        return coupon.getDiscountType() == DiscountType.PERCENTAGE;
    }

    @Override
    public BigDecimal calculate(BigDecimal amount, DiscountCoupon coupon) {
        BigDecimal discount = amount
                .multiply(coupon.getDiscountValue())
                .divide(MoneyUtils.ONE_HUNDRED, 2, RoundingMode.HALF_UP);
        if (coupon.getMaxDiscountAmount() != null) {
            discount = discount.min(coupon.getMaxDiscountAmount());
        }
        return MoneyUtils.money(discount);
    }
}
