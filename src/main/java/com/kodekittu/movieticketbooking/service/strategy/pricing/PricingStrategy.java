package com.kodekittu.movieticketbooking.service.strategy.pricing;

import com.kodekittu.movieticketbooking.entity.PricingTier;
import com.kodekittu.movieticketbooking.entity.ShowSeat;

import java.math.BigDecimal;

public interface PricingStrategy {

    boolean supports(PricingTier tier, ShowSeat showSeat);

    BigDecimal apply(BigDecimal currentPrice, PricingTier tier, ShowSeat showSeat);
}
