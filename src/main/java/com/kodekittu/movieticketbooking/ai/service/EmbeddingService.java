package com.kodekittu.movieticketbooking.ai.service;

import com.kodekittu.movieticketbooking.ai.client.GeminiClient;
import com.kodekittu.movieticketbooking.ai.client.VectorStoreClient;
import com.kodekittu.movieticketbooking.ai.embedding.MovieEmbeddingGenerator;
import com.kodekittu.movieticketbooking.entity.Movie;
import com.kodekittu.movieticketbooking.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmbeddingService {

    private final MovieRepository movieRepository;
    private final GeminiClient geminiClient;
    private final VectorStoreClient vectorStoreClient;
    private final MovieEmbeddingGenerator movieEmbeddingGenerator;

    @Transactional(readOnly = true)
    public void indexAllMovies() {

        List<Movie> movies = movieRepository.findAll();

        log.info("Starting movie indexing. Total movies: {}", movies.size());

        for (Movie movie : movies) {

            try {

                indexMovie(movie);

            } catch (Exception ex) {

                log.error("Failed to index movie : {}", movie.getTitle(), ex);

            }

        }

        log.info("Movie indexing completed.");

    }

    @Transactional(readOnly = true)
    public void indexMovie(UUID movieId) {

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() ->
                        new RuntimeException("Movie not found : " + movieId));

        indexMovie(movie);

    }

    private void indexMovie(Movie movie) {

        String embeddingText = movieEmbeddingGenerator.generate(movie);

        List<Double> embedding =
                geminiClient.generateEmbedding(embeddingText);

        Map<String, String> metadata = new HashMap<>();

        metadata.put("movieId", movie.getId().toString());
        metadata.put("title", movie.getTitle());
        metadata.put("genre", movie.getGenre());
        metadata.put("language", movie.getLanguage());
        metadata.put("certificate", movie.getCertificate());

        vectorStoreClient.upsert(
                movie.getId().toString(),
                embedding,
                metadata
        );

        log.info("Indexed movie : {}", movie.getTitle());

    }

}