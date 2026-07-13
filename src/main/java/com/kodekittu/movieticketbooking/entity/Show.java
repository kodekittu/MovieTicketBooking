package com.kodekittu.movieticketbooking.entity;

import com.kodekittu.movieticketbooking.entity.enums.ShowStatus;
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

import java.time.Instant;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "shows",
        uniqueConstraints = @UniqueConstraint(name = "uk_shows_screen_start_time", columnNames = {"screen_id", "start_time"}),
        indexes = {
                @Index(name = "idx_shows_movie_id", columnList = "movie_id"),
                @Index(name = "idx_shows_screen_id", columnList = "screen_id"),
                @Index(name = "idx_shows_start_time", columnList = "start_time")
        })
public class Show extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id", nullable = false, foreignKey = @ForeignKey(name = "fk_shows_movie"))
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "screen_id", nullable = false, foreignKey = @ForeignKey(name = "fk_shows_screen"))
    private Screen screen;

    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "end_time", nullable = false)
    private Instant endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private ShowStatus status = ShowStatus.SCHEDULED;
}
