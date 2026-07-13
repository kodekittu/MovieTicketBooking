package com.kodekittu.movieticketbooking.ai.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kodekittu.movieticketbooking.ai.config.GeminiProperties;
import com.kodekittu.movieticketbooking.ai.dto.LLMRequest;
import com.kodekittu.movieticketbooking.ai.dto.LLMResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiClient implements LLMClient {

    private final RestClient restClient;
    private final GeminiProperties properties;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() {

        log.info("Gemini Base URL : {}", properties.getBaseUrl());
        log.info("Gemini Model : {}", properties.getModel());
        log.info("Embedding Model : {}", properties.getEmbeddingModel());

    }

    @Override
    public LLMResponse generateResponse(LLMRequest request) {

        String url = String.format(
                "%s/v1beta/models/%s:generateContent?key=%s",
                properties.getBaseUrl(),
                properties.getModel(),
                properties.getApiKey()
        );

        Map<String, Object> body = Map.of(
                "contents",
                List.of(
                        Map.of(
                                "parts",
                                List.of(
                                        Map.of(
                                                "text",
                                                request.getPrompt()
                                        )
                                )
                        )
                )
        );

        JsonNode response = restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(JsonNode.class);

        String text = response
                .path("candidates")
                .get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text")
                .asText();

        return LLMResponse.builder()
                .response(text)
                .build();
    }

    @Override
    public List<Double> generateEmbedding(String text) {

        String url = String.format(
                "%s/v1beta/models/%s:embedContent?key=%s",
                properties.getBaseUrl(),
                properties.getEmbeddingModel(),
                properties.getApiKey()
        );

        Map<String, Object> body = Map.of(
                "content",
                Map.of(
                        "parts",
                        List.of(
                                Map.of(
                                        "text",
                                        text
                                )
                        )
                )
        );

        JsonNode response = restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(JsonNode.class);

        JsonNode values = response
                .path("embedding")
                .path("values");

        List<Double> embedding = new ArrayList<>();

        values.forEach(node -> embedding.add(node.asDouble()));

        return embedding;
    }
}