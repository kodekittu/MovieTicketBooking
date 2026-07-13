package com.kodekittu.movieticketbooking.ai.service;

import com.kodekittu.movieticketbooking.ai.client.GeminiClient;
import com.kodekittu.movieticketbooking.ai.client.PineconeClient;
import com.kodekittu.movieticketbooking.ai.dto.LLMRequest;
import com.kodekittu.movieticketbooking.ai.dto.LLMResponse;
import com.kodekittu.movieticketbooking.ai.dto.RecommendationResponse;
import com.kodekittu.movieticketbooking.ai.dto.VectorSearchResult;
import com.kodekittu.movieticketbooking.ai.prompt.RecommendationPromptBuilder;
import com.kodekittu.movieticketbooking.entity.Booking;
import com.kodekittu.movieticketbooking.entity.Movie;
import com.kodekittu.movieticketbooking.entity.Recommendation;
import com.kodekittu.movieticketbooking.entity.User;
import com.kodekittu.movieticketbooking.repository.BookingRepository;
import com.kodekittu.movieticketbooking.repository.MovieRepository;
import com.kodekittu.movieticketbooking.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIRecommendationService {

    private final BookingRepository bookingRepository;
    private final MovieRepository movieRepository;
    private final GeminiClient geminiClient;
    private final PineconeClient pineconeClient;
    private final RecommendationPromptBuilder promptBuilder;
    private final RecommendationRepository recommendationRepository;

    public String generateRecommendation(User user) {

        List<Booking> bookings =
                bookingRepository.findByUserId(user.getId());

        if (bookings.isEmpty()) {
            return "No recommendations available.";
        }

        String genres = bookings.stream()
                .map(Booking::getShow)
                .map(show -> show.getMovie().getGenre())
                .distinct()
                .collect(Collectors.joining(", "));

        List<Double> embedding =
                geminiClient.generateEmbedding(genres);

        List<VectorSearchResult> results =
                pineconeClient.similaritySearch(
                        embedding,
                        5
                );

        List<Movie> movies = results.stream()
                .map(result ->
                        movieRepository.findById(
                                UUID.fromString(result.getMetadata().get("movieId"))
                        ).orElse(null))
                .filter(movie -> movie != null)
                .toList();

        String prompt =
                promptBuilder.build(user, movies);

        LLMResponse response =
                geminiClient.generateResponse(
                        LLMRequest.builder()
                                .prompt(prompt)
                                .build()
                );

        Recommendation recommendation = Recommendation.builder()
                .user(user)
                .recommendationText(response.getResponse())
                .build();

        recommendationRepository.save(recommendation);

        return recommendation.getRecommendationText();

    }

    @Transactional(readOnly = true)
    public List<RecommendationResponse> getRecommendations(UUID userId) {

        return recommendationRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(r -> RecommendationResponse.builder()
                        .id(r.getId())
                        .recommendationText(r.getRecommendationText())
                        .createdAt(r.getCreatedAt())
                        .build())
                .toList();

    }

}