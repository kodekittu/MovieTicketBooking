package com.kodekittu.movieticketbooking.service.strategy.pricing;

import com.kodekittu.movieticketbooking.entity.PricingTier;
import com.kodekittu.movieticketbooking.entity.ShowSeat;
import com.kodekittu.movieticketbooking.entity.enums.PricingRuleType;
import com.kodekittu.movieticketbooking.util.MoneyUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ShowSpecificPricingStrategy implements PricingStrategy {

    @Override
    public boolean supports(PricingTier tier, ShowSeat showSeat) {
        return tier.getRuleType() == PricingRuleType.SHOW_SPECIFIC
                && tier.getShow() != null
                && tier.getShow().getId().equals(showSeat.getShow().getId());
    }

    @Override
    public BigDecimal apply(BigDecimal currentPrice, PricingTier tier, ShowSeat showSeat) {
        return tier.getBasePrice() == null ? currentPrice : MoneyUtils.money(tier.getBasePrice());
    }
}
