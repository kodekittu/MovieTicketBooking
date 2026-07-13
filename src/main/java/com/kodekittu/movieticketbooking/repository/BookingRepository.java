package com.kodekittu.movieticketbooking.repository;

import com.kodekittu.movieticketbooking.entity.Booking;
import com.kodekittu.movieticketbooking.entity.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    Optional<Booking> findByBookingReference(String bookingReference);

    List<Booking> findBySeatHoldId(UUID seatHoldId);

    List<Booking> findByUserId(UUID userId);

    Page<Booking> findByUserId(UUID userId, Pageable pageable);

    Page<Booking> findByUserIdAndStatus(UUID userId,
                                        BookingStatus status,
                                        Pageable pageable);

    @EntityGraph(attributePaths = {
            "user",
            "show",
            "show.movie",
            "show.screen",
            "show.screen.theater"
    })
    Optional<Booking> findWithDetailsById(UUID id);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select b from Booking b where b.id = :id")
    Optional<Booking> findByIdForOptimisticUpdate(@Param("id") UUID id);
}
