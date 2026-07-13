package com.kodekittu.movieticketbooking.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VectorSearchResult {

    private String id;

    private Double score;

    private Map<String, String> metadata;

}