package com.kodekittu.movieticketbooking.repository;

import com.kodekittu.movieticketbooking.entity.Show;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ShowRepository extends JpaRepository<Show, UUID> {

    Page<Show> findByMovieIdAndStartTimeBetween(UUID movieId, Instant from, Instant to, Pageable pageable);

    @Query("""
            select s from Show s
            join s.screen sc
            join sc.theater t
            where (:movieId is null or s.movie.id = :movieId)
              and (:cityId is null or t.city.id = :cityId)
              and s.startTime >= :from
              and s.startTime < :to
            """)
    Page<Show> searchShows(
            @Param("movieId") UUID movieId,
            @Param("cityId") UUID cityId,
            @Param("from") Instant from,
            @Param("to") Instant to,
            Pageable pageable);

    @Query("""
            select s from Show s
            where s.screen.id = :screenId
              and s.startTime < :endTime
              and s.endTime > :startTime
            """)
    List<Show> findOverlappingShows(
            @Param("screenId") UUID screenId,
            @Param("startTime") Instant startTime,
            @Param("endTime") Instant endTime);
}
