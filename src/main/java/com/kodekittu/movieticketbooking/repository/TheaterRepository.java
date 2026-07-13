package com.kodekittu.movieticketbooking.repository;

import com.kodekittu.movieticketbooking.entity.Theater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TheaterRepository extends JpaRepository<Theater, UUID> {

    Page<Theater> findByActiveTrue(Pageable pageable);

    Page<Theater> findByCityIdAndActiveTrue(UUID cityId, Pageable pageable);

    Page<Theater> findByCityIdAndActiveTrueAndNameContainingIgnoreCase(UUID cityId, String name, Pageable pageable);
}
