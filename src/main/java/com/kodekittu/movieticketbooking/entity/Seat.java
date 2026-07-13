package com.kodekittu.movieticketbooking.entity;

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
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "seats",
        uniqueConstraints = @UniqueConstraint(name = "uk_seats_screen_row_number", columnNames = {"screen_id", "row_label", "seat_number"}),
        indexes = {
                @Index(name = "idx_seats_screen_id", columnList = "screen_id"),
                @Index(name = "idx_seats_screen_type", columnList = "screen_id, seat_type")
        })
public class Seat extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "screen_id", nullable = false, foreignKey = @ForeignKey(name = "fk_seats_screen"))
    private Screen screen;

    @Column(name = "row_label", nullable = false, length = 10)
    private String rowLabel;

    @Column(name = "seat_number", nullable = false)
    private int seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_type", nullable = false, length = 50)
    private SeatType seatType = SeatType.REGULAR;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    public String label() {
        return rowLabel + seatNumber;
    }
}
