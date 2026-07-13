package com.kodekittu.movieticketbooking.service.strategy.pricing;

import com.kodekittu.movieticketbooking.entity.PricingTier;
import com.kodekittu.movieticketbooking.entity.ShowSeat;
import com.kodekittu.movieticketbooking.entity.enums.PricingRuleType;
import com.kodekittu.movieticketbooking.util.MoneyUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.ZoneOffset;

@Component
public class WeekendPricingStrategy implements PricingStrategy {

    @Override
    public boolean supports(PricingTier tier, ShowSeat showSeat) {
        DayOfWeek showDay = showSeat.getShow().getStartTime().atZone(ZoneOffset.UTC).getDayOfWeek();
        boolean weekend = showDay == DayOfWeek.SATURDAY || showDay == DayOfWeek.SUNDAY;
        return tier.getRuleType() == PricingRuleType.WEEKEND
                && weekend
                && (tier.getDayOfWeek() == null || tier.getDayOfWeek() == showDay);
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
