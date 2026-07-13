package com.kodekittu.movieticketbooking.mapper;

import com.kodekittu.movieticketbooking.dto.request.PricingTierRequest;
import com.kodekittu.movieticketbooking.dto.response.PricingTierResponse;
import com.kodekittu.movieticketbooking.entity.Movie;
import com.kodekittu.movieticketbooking.entity.PricingTier;
import com.kodekittu.movieticketbooking.entity.Screen;
import com.kodekittu.movieticketbooking.entity.Show;
import com.kodekittu.movieticketbooking.entity.Theater;
import org.springframework.stereotype.Component;

@Component
public class PricingTierMapper {

    public PricingTier toEntity(PricingTierRequest request, Movie movie, Theater theater, Screen screen, Show show) {
        PricingTier tier = new PricingTier();
        update(tier, request, movie, theater, screen, show);
        return tier;
    }

    public void update(PricingTier tier, PricingTierRequest request, Movie movie, Theater theater, Screen screen, Show show) {
        tier.setName(request.name());
        tier.setRuleType(request.ruleType());
        tier.setSeatType(request.seatType());
        tier.setDayOfWeek(request.dayOfWeek());
        tier.setMovie(movie);
        tier.setTheater(theater);
        tier.setScreen(screen);
        tier.setShow(show);
        tier.setBasePrice(request.basePrice());
        tier.setMultiplier(request.multiplier());
        tier.setPriority(request.priority());
        tier.setActive(request.active());
        tier.setValidFrom(request.validFrom());
        tier.setValidUntil(request.validUntil());
    }

    public PricingTierResponse toResponse(PricingTier tier) {
        return new PricingTierResponse(
                tier.getId(),
                tier.getName(),
                tier.getRuleType(),
                tier.getSeatType(),
                tier.getDayOfWeek(),
                tier.getMovie() == null ? null : tier.getMovie().getId(),
                tier.getTheater() == null ? null : tier.getTheater().getId(),
                tier.getScreen() == null ? null : tier.getScreen().getId(),
                tier.getShow() == null ? null : tier.getShow().getId(),
                tier.getBasePrice(),
                tier.getMultiplier(),
                tier.getPriority(),
                tier.isActive(),
                tier.getValidFrom(),
                tier.getValidUntil(),
                tier.getCreatedAt(),
                tier.getUpdatedAt());
    }
}
