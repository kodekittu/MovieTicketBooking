package com.kodekittu.movieticketbooking.entity;

import com.kodekittu.movieticketbooking.entity.enums.RefundScope;
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

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "refund_policies", indexes = {
        @Index(name = "idx_refund_policies_active", columnList = "active"),
        @Index(name = "idx_refund_policies_priority", columnList = "priority"),
        @Index(name = "idx_refund_policies_theater_id", columnList = "theater_id"),
        @Index(name = "idx_refund_policies_movie_id", columnList = "movie_id")
})
public class RefundPolicy extends BaseEntity {

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "scope", nullable = false, length = 50)
    private RefundScope scope = RefundScope.GLOBAL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", foreignKey = @ForeignKey(name = "fk_refund_policies_movie"))
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", foreignKey = @ForeignKey(name = "fk_refund_policies_theater"))
    private Theater theater;

    @Column(name = "hours_before_show", nullable = false)
    private int hoursBeforeShow;

    @Column(name = "refund_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal refundPercentage;

    @Column(name = "priority", nullable = false)
    private int priority;

    @Column(name = "active", nullable = false)
    private boolean active = true;
}
