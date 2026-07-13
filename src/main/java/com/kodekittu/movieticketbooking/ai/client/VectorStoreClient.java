package com.kodekittu.movieticketbooking.ai.client;

import com.kodekittu.movieticketbooking.ai.dto.VectorSearchResult;

import java.util.List;
import java.util.Map;

public interface VectorStoreClient {

    void upsert(
            String id,
            List<Double> embedding,
            Map<String, String> metadata
    );

    List<VectorSearchResult> similaritySearch(
            List<Double> embedding,
            int topK
    );

}