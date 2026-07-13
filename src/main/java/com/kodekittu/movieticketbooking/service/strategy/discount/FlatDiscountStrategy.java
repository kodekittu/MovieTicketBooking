package com.kodekittu.movieticketbooking.service.strategy.discount;

import com.kodekittu.movieticketbooking.entity.DiscountCoupon;
import com.kodekittu.movieticketbooking.entity.enums.DiscountType;
import com.kodekittu.movieticketbooking.util.MoneyUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FlatDiscountStrategy implements DiscountStrategy {

    @Override
    public boolean supports(DiscountCoupon coupon) {
        return coupon.getDiscountType() == DiscountType.FLAT;
    }

    @Override
    public BigDecimal calculate(BigDecimal amount, DiscountCoupon coupon) {
        return MoneyUtils.money(coupon.getDiscountValue().min(amount));
    }
}
