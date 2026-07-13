package com.kodekittu.movieticketbooking.repository;

import com.kodekittu.movieticketbooking.entity.SeatHold;
import com.kodekittu.movieticketbooking.entity.enums.SeatHoldStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SeatHoldRepository extends JpaRepository<SeatHold, UUID> {

    Optional<SeatHold> findByIdAndUserId(UUID id, UUID userId);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select h from SeatHold h where h.id = :id")
    Optional<SeatHold> findByIdForOptimisticUpdate(@Param("id") UUID id);

    List<SeatHold> findByStatusAndExpiresAtLessThanEqual(SeatHoldStatus status, Instant expiresAt, Pageable pageable);
}
