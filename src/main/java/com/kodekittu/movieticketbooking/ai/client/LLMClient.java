package com.kodekittu.movieticketbooking.ai.client;

import com.kodekittu.movieticketbooking.ai.dto.EmbeddingRequest;
import com.kodekittu.movieticketbooking.ai.dto.EmbeddingResponse;
import com.kodekittu.movieticketbooking.ai.dto.LLMRequest;
import com.kodekittu.movieticketbooking.ai.dto.LLMResponse;

import java.util.List;

public interface LLMClient {

    LLMResponse generateResponse(LLMRequest request);

    public List<Double> generateEmbedding(String text);

}