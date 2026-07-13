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
@Table(name = "theaters",
        uniqueConstraints = @UniqueConstraint(name = "uk_theaters_city_name_address", columnNames = {"city_id", "name", "address"}),
        indexes = {
                @Index(name = "idx_theaters_city_id", columnList = "city_id"),
                @Index(name = "idx_theaters_active", columnList = "active")
        })
public class Theater extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_id", nullable = false, foreignKey = @ForeignKey(name = "fk_theaters_city"))
    private City city;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "address", nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(name = "active", nullable = false)
    private boolean active = true;
}
