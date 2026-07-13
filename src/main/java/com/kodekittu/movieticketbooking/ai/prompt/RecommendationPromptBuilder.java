package com.kodekittu.movieticketbooking.ai.prompt;

import com.kodekittu.movieticketbooking.entity.Movie;
import com.kodekittu.movieticketbooking.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RecommendationPromptBuilder {

    public String build(User user,
                        List<Movie> movies) {

        String movieList = movies.stream()
                .map(movie ->
                        movie.getTitle()
                                + " ("
                                + movie.getGenre()
                                + ")")
                .collect(Collectors.joining("\n"));

        return """
                You are a movie recommendation assistant.

                Recommend 3 movies.

                User Name:
                %s

                Candidate Movies:

                %s

                Explain in 2-3 sentences why each movie is recommended.

                Keep the response short.
                """
                .formatted(
                        user.getName(),
                        movieList
                );
    }

}