package com.kodekittu.movieticketbooking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "booking_seats",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_booking_seats_booking_show_seat", columnNames = {"booking_id", "show_seat_id"})
        },
        indexes = {
                @Index(name = "idx_booking_seats_booking_id", columnList = "booking_id"),
                @Index(name = "idx_booking_seats_show_seat_id", columnList = "show_seat_id")
        })
public class BookingSeat extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", nullable = false, foreignKey = @ForeignKey(name = "fk_booking_seats_booking"))
    private Booking booking;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "show_seat_id", nullable = false, foreignKey = @ForeignKey(name = "fk_booking_seats_show_seat"))
    private ShowSeat showSeat;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seat_id", nullable = false, foreignKey = @ForeignKey(name = "fk_booking_seats_seat"))
    private Seat seat;

    @Column(name = "seat_label", nullable = false, length = 20)
    private String seatLabel;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
}
