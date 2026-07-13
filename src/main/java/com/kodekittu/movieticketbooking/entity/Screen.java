package com.kodekittu.movieticketbooking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "screens",
        uniqueConstraints = @UniqueConstraint(name = "uk_screens_theater_name", columnNames = {"theater_id", "name"}),
        indexes = @Index(name = "idx_screens_theater_id", columnList = "theater_id"))
public class Screen extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "theater_id", nullable = false, foreignKey = @ForeignKey(name = "fk_screens_theater"))
    private Theater theater;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "screen_type", length = 50)
    private String screenType;

    @Column(name = "active", nullable = false)
    private boolean active = true;
}
