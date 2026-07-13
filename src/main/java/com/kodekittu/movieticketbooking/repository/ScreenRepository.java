package com.kodekittu.movieticketbooking.repository;

import com.kodekittu.movieticketbooking.entity.Screen;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ScreenRepository extends JpaRepository<Screen, UUID> {

    List<Screen> findByTheaterIdAndActiveTrue(UUID theaterId);

    Page<Screen> findByTheaterId(UUID theaterId, Pageable pageable);
}
