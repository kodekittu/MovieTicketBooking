package com.kodekittu.movieticketbooking.repository;

import com.kodekittu.movieticketbooking.entity.ShowSeat;
import com.kodekittu.movieticketbooking.entity.enums.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ShowSeatRepository extends JpaRepository<ShowSeat, UUID> {

    List<ShowSeat> findByShowIdOrderBySeatRowLabelAscSeatSeatNumberAsc(UUID showId);

    List<ShowSeat> findByShowIdAndSeatIdIn(UUID showId, Collection<UUID> seatIds);

    List<ShowSeat> findBySeatHoldId(UUID seatHoldId);

    List<ShowSeat> findByBookingId(UUID bookingId);

    long countByShowIdAndStatus(UUID showId, SeatStatus status);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("""
            select ss from ShowSeat ss
            where ss.show.id = :showId
              and ss.seat.id in :seatIds
            """)
    List<ShowSeat> findForOptimisticUpdate(
            @Param("showId") UUID showId,
            @Param("seatIds") Collection<UUID> seatIds);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query("""
            select ss from ShowSeat ss
            where ss.seatHold.id = :seatHoldId
            """)
    List<ShowSeat> findHeldSeatsForConversion(@Param("seatHoldId") UUID seatHoldId);
}
