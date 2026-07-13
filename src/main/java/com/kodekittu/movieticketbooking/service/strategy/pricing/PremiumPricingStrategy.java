package com.kodekittu.movieticketbooking.service.strategy.pricing;

import com.kodekittu.movieticketbooking.entity.PricingTier;
import com.kodekittu.movieticketbooking.entity.ShowSeat;
import com.kodekittu.movieticketbooking.entity.enums.PricingRuleType;
import com.kodekittu.movieticketbooking.util.MoneyUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PremiumPricingStrategy implements PricingStrategy {

    @Override
    public boolean supports(PricingTier tier, ShowSeat showSeat) {
        return tier.getRuleType() == PricingRuleType.PREMIUM
                && tier.getSeatType() != null
                && tier.getSeatType() == showSeat.getSeat().getSeatType();
    }

    @Override
    public BigDecimal apply(BigDecimal currentPrice, PricingTier tier, ShowSeat showSeat) {
        BigDecimal price = tier.getBasePrice() == null ? currentPrice : tier.getBasePrice();
        if (tier.getMultiplier() != null) {
            price = price.multiply(tier.getMultiplier());
        }
        return MoneyUtils.money(price);
    }
}
