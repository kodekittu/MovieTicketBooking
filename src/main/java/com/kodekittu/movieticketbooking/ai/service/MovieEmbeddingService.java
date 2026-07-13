package com.kodekittu.movieticketbooking.ai.service;

import com.kodekittu.movieticketbooking.ai.client.GeminiClient;
import com.kodekittu.movieticketbooking.ai.client.PineconeClient;
import com.kodekittu.movieticketbooking.ai.embedding.MovieEmbeddingGenerator;
import com.kodekittu.movieticketbooking.entity.Movie;
import com.kodekittu.movieticketbooking.exception.ResourceNotFoundException;
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
public class MovieEmbeddingService {

    private final MovieRepository movieRepository;
    private final GeminiClient geminiClient;
    private final PineconeClient pineconeClient;
    private final MovieEmbeddingGenerator movieEmbeddingGenerator;

    @Transactional(readOnly = true)
    public void indexAllMovies() {

        List<Movie> movies = movieRepository.findAll();

        log.info("Starting indexing for {} movies.", movies.size());

        int success = 0;
        int failed = 0;

        for (Movie movie : movies) {

            if (!movie.isActive()) {
                continue;
            }

            try {

                indexMovie(movie);

                success++;

            } catch (Exception ex) {

                failed++;

                log.error(
                        "Failed to index movie {}",
                        movie.getTitle(),
                        ex
                );
            }

        }

        log.info(
                "Movie indexing completed. Success: {}, Failed: {}",
                success,
                failed
        );

    }

    @Transactional(readOnly = true)
    public void indexMovie(UUID movieId) {

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Movie not found : " + movieId
                        ));

        indexMovie(movie);

    }

    private void indexMovie(Movie movie) {

        String embeddingText =
                movieEmbeddingGenerator.generate(movie);

        List<Double> embedding =
                geminiClient.generateEmbedding(embeddingText);

        Map<String, String> metadata = buildMetadata(movie);

        pineconeClient.upsert(
                movie.getId().toString(),
                embedding,
                metadata
        );

        log.info(
                "Successfully indexed movie : {}",
                movie.getTitle()
        );

    }

    private Map<String, String> buildMetadata(Movie movie) {

        Map<String, String> metadata = new HashMap<>();

        metadata.put("movieId", movie.getId().toString());
        metadata.put("title", movie.getTitle());
        metadata.put("genre", movie.getGenre());
        metadata.put("language", movie.getLanguage());
        metadata.put("certificate", movie.getCertificate());

        return metadata;

    }

}