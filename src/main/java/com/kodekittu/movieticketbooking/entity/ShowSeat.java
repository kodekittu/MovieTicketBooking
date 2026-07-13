package com.kodekittu.movieticketbooking.entity;

import com.kodekittu.movieticketbooking.entity.enums.SeatStatus;
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
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "show_seats",
        uniqueConstraints = @UniqueConstraint(name = "uk_show_seats_show_seat", columnNames = {"show_id", "seat_id"}),
        indexes = {
                @Index(name = "idx_show_seats_show_id", columnList = "show_id"),
                @Index(name = "idx_show_seats_status", columnList = "show_id, status"),
                @Index(name = "idx_show_seats_hold_id", columnList = "seat_hold_id"),
                @Index(name = "idx_show_seats_booking_id", columnList = "booking_id")
        })
public class ShowSeat extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "show_id", nullable = false, foreignKey = @ForeignKey(name = "fk_show_seats_show"))
    private Show show;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seat_id", nullable = false, foreignKey = @ForeignKey(name = "fk_show_seats_seat"))
    private Seat seat;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private SeatStatus status = SeatStatus.AVAILABLE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_hold_id", foreignKey = @ForeignKey(name = "fk_show_seats_seat_hold"))
    private SeatHold seatHold;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", foreignKey = @ForeignKey(name = "fk_show_seats_booking"))
    private Booking booking;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price = BigDecimal.ZERO;

    @Version
    @Column(name = "version", nullable = false)
    private long version;
}
