package com.kodekittu.movieticketbooking.ai.embedding;

import com.kodekittu.movieticketbooking.entity.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieEmbeddingGenerator {

    public String generate(Movie movie) {

        return """
                Movie Title: %s

                Genre: %s

                Language: %s

                Duration: %d minutes

                Certificate: %s

                Description:
                %s
                """.formatted(
                movie.getTitle(),
                movie.getGenre(),
                movie.getLanguage(),
                movie.getDurationMinutes(),
                movie.getCertificate(),
                movie.getDescription()
        );

    }

}