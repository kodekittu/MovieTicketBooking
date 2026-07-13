package com.kodekittu.movieticketbooking.repository;

import com.kodekittu.movieticketbooking.entity.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CityRepository extends JpaRepository<City, UUID> {

    Page<City> findByActiveTrueAndNameContainingIgnoreCase(String name, Pageable pageable);

    Page<City> findByActiveTrue(Pageable pageable);
}
