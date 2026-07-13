package com.kodekittu.movieticketbooking.ai.dto;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationResponse {

    private UUID id;

    private String recommendationText;

    private Instant createdAt;

}