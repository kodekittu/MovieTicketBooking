package com.kodekittu.movieticketbooking.ai.controller;

import com.kodekittu.movieticketbooking.ai.client.GeminiClient;
import com.kodekittu.movieticketbooking.ai.client.PineconeClient;
import com.kodekittu.movieticketbooking.ai.dto.*;
import com.kodekittu.movieticketbooking.ai.service.AIChatService;
import com.kodekittu.movieticketbooking.ai.service.AIRecommendationService;
import com.kodekittu.movieticketbooking.ai.service.MovieEmbeddingService;
import com.kodekittu.movieticketbooking.repository.RecommendationRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {

    private final RecommendationRepository recommendationRepository;
    private final AIChatService aiChatService;
    private final AIRecommendationService recommendationService;
    private final MovieEmbeddingService embeddingService;
    private final GeminiClient geminiClient;
    private final PineconeClient pineconeClient;

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(
            @Valid @RequestBody ChatRequest request) {

        return ResponseEntity.ok(
                aiChatService.chat(request)
        );
    }

    @GetMapping("/recommendations/{userId}")
    public ResponseEntity<List<RecommendationResponse>> recommendations(
            @PathVariable UUID userId) {

        return ResponseEntity.ok(
                recommendationService.getRecommendations(userId)
        );
    }

    @PostMapping("/reindex")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> reindex() {

        embeddingService.indexAllMovies();

        return ResponseEntity.ok("Movie indexing completed.");

    }


    // endpoint for testing gemini integration

    @PostMapping("/test-gemini")
    public String testGemini() {

        return geminiClient.generateResponse(
                LLMRequest.builder()
                        .prompt("Say Hello from Gemini")
                        .build()
        ).getResponse();

    }

    @GetMapping("/test-embedding")
    public List<Double> embedding() {

        return geminiClient.generateEmbedding(
                "Interstellar is a science fiction movie."
        );

    }

    // endpoint for testing pinecone integration

    @PostMapping("/test-pinecone")
    public ResponseEntity<String> testPinecone() {

        List<Double> embedding = geminiClient.generateEmbedding(
                "Interstellar is a science fiction movie."
        );

        pineconeClient.upsert(
                UUID.randomUUID().toString(),
                embedding,
                Map.of(
                        "title", "Interstellar",
                        "genre", "Sci-Fi",
                        "language", "English",
                        "movieId", UUID.randomUUID().toString()
                )
        );

        return ResponseEntity.ok("Inserted into Pinecone successfully");
    }

    @GetMapping("/test-search")
    public ResponseEntity<List<VectorSearchResult>> testSearch() {

        List<Double> embedding = geminiClient.generateEmbedding(
                "A science fiction movie about astronauts in space"
        );

        List<VectorSearchResult> results =
                pineconeClient.similaritySearch(
                        embedding,
                        5
                );

        return ResponseEntity.ok(results);
    }

}