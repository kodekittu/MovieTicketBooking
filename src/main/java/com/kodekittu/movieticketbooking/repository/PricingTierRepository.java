package com.kodekittu.movieticketbooking.repository;

import com.kodekittu.movieticketbooking.entity.PricingTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface PricingTierRepository extends JpaRepository<PricingTier, UUID> {

    @Query("""
            select p from PricingTier p
            where p.active = true
              and (p.validFrom is null or p.validFrom <= :at)
              and (p.validUntil is null or p.validUntil >= :at)
              and (p.show is null or p.show.id = :showId)
              and (p.screen is null or p.screen.id = :screenId)
              and (p.theater is null or p.theater.id = :theaterId)
              and (p.movie is null or p.movie.id = :movieId)
            order by p.priority desc
            """)
    List<PricingTier> findApplicablePricingTiers(
            @Param("movieId") UUID movieId,
            @Param("theaterId") UUID theaterId,
            @Param("screenId") UUID screenId,
            @Param("showId") UUID showId,
            @Param("at") Instant at);
}
