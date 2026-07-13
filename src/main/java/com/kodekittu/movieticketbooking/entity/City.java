package com.kodekittu.movieticketbooking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "cities",
        uniqueConstraints = @UniqueConstraint(name = "uk_cities_name_state_country", columnNames = {"name", "state", "country"}),
        indexes = @Index(name = "idx_cities_name", columnList = "name"))
public class City extends BaseEntity {

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "state", length = 150)
    private String state;

    @Column(name = "country", nullable = false, length = 150)
    private String country;

    @Column(name = "active", nullable = false)
    private boolean active = true;
}
