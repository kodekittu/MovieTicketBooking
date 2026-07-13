package com.kodekittu.movieticketbooking.repository;

import com.kodekittu.movieticketbooking.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RecommendationRepository
        extends JpaRepository<Recommendation, Long> {

    List<Recommendation> findByUserIdOrderByCreatedAtDesc(UUID userId);

}