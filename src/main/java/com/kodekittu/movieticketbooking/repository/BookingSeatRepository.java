package com.kodekittu.movieticketbooking.repository;

import com.kodekittu.movieticketbooking.entity.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, UUID> {

    List<BookingSeat> findByBookingId(UUID bookingId);
}
