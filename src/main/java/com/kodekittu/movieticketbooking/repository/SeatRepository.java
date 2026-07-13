package com.kodekittu.movieticketbooking.repository;

import com.kodekittu.movieticketbooking.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SeatRepository extends JpaRepository<Seat, UUID> {

    List<Seat> findByScreenIdAndActiveTrueOrderByRowLabelAscSeatNumberAsc(UUID screenId);

    long countByScreenIdAndActiveTrue(UUID screenId);
}
