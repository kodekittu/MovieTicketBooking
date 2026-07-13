package com.kodekittu.movieticketbooking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "movies", indexes = {
        @Index(name = "idx_movies_title", columnList = "title"),
        @Index(name = "idx_movies_language", columnList = "language"),
        @Index(name = "idx_movies_active", columnList = "active")
})
public class Movie extends BaseEntity {

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "language", nullable = false, length = 50)
    private String language;

    @Column(name = "genre", length = 100)
    private String genre;

    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;

    @Column(name = "certificate", length = 20)
    private String certificate;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "active", nullable = false)
    private boolean active = true;
}
