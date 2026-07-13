package com.kodekittu.movieticketbooking.entity;

import com.kodekittu.movieticketbooking.entity.enums.PricingRuleType;
import com.kodekittu.movieticketbooking.entity.enums.SeatType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Instant;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "pricing_tiers", indexes = {
        @Index(name = "idx_pricing_tiers_active", columnList = "active"),
        @Index(name = "idx_pricing_tiers_priority", columnList = "priority"),
        @Index(name = "idx_pricing_tiers_show_id", columnList = "show_id"),
        @Index(name = "idx_pricing_tiers_theater_id", columnList = "theater_id"),
        @Index(name = "idx_pricing_tiers_movie_id", columnList = "movie_id")
})
public class PricingTier extends BaseEntity {

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "rule_type", nullable = false, length = 50)
    private PricingRuleType ruleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_type", length = 50)
    private SeatType seatType;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", length = 20)
    private DayOfWeek dayOfWeek;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", foreignKey = @ForeignKey(name = "fk_pricing_tiers_movie"))
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", foreignKey = @ForeignKey(name = "fk_pricing_tiers_theater"))
    private Theater theater;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id", foreignKey = @ForeignKey(name = "fk_pricing_tiers_screen"))
    private Screen screen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_id", foreignKey = @ForeignKey(name = "fk_pricing_tiers_show"))
    private Show show;

    @Column(name = "base_price", precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "multiplier", precision = 5, scale = 2)
    private BigDecimal multiplier;

    @Column(name = "priority", nullable = false)
    private int priority;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "valid_from")
    private Instant validFrom;

    @Column(name = "valid_until")
    private Instant validUntil;
}
