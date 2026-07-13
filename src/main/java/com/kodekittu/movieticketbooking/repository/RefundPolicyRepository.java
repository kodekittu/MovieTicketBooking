package com.kodekittu.movieticketbooking.repository;

import com.kodekittu.movieticketbooking.entity.RefundPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RefundPolicyRepository extends JpaRepository<RefundPolicy, UUID> {

    @Query("""
            select r from RefundPolicy r
            where r.active = true
              and r.hoursBeforeShow <= :hoursBeforeShow
              and (r.movie is null or r.movie.id = :movieId)
              and (r.theater is null or r.theater.id = :theaterId)
            order by r.priority desc, r.hoursBeforeShow desc
            """)
    List<RefundPolicy> findApplicablePolicies(
            @Param("movieId") UUID movieId,
            @Param("theaterId") UUID theaterId,
            @Param("hoursBeforeShow") long hoursBeforeShow);
}
