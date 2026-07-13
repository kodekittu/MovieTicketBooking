package com.kodekittu.movieticketbooking.ai.controller;

import com.kodekittu.movieticketbooking.ai.service.AIChatService;
import com.kodekittu.movieticketbooking.ai.dto.ChatRequest;
import com.kodekittu.movieticketbooking.ai.dto.ChatResponse;
import com.kodekittu.movieticketbooking.ai.dto.RecommendationResponse;
import com.kodekittu.movieticketbooking.ai.service.AIRecommendationService;
import com.kodekittu.movieticketbooking.ai.service.MovieEmbeddingService;
import com.kodekittu.movieticketbooking.repository.RecommendationRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {

    private final RecommendationRepository recommendationRepository;
    private final AIChatService aiChatService;
    private final AIRecommendationService recommendationService;
    private final MovieEmbeddingService embeddingService;

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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> reindex() {

        embeddingService.indexAllMovies();

        return ResponseEntity.ok("Movie indexing completed.");

    }

}