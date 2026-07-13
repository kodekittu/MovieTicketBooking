package com.kodekittu.movieticketbooking.mapper;

import com.kodekittu.movieticketbooking.dto.request.MovieRequest;
import com.kodekittu.movieticketbooking.dto.response.MovieResponse;
import com.kodekittu.movieticketbooking.entity.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

    public Movie toEntity(MovieRequest request) {
        Movie movie = new Movie();
        update(movie, request);
        return movie;
    }

    public void update(Movie movie, MovieRequest request) {
        movie.setTitle(request.title());
        movie.setDescription(request.description());
        movie.setLanguage(request.language());
        movie.setGenre(request.genre());
        movie.setDurationMinutes(request.durationMinutes());
        movie.setCertificate(request.certificate());
        movie.setReleaseDate(request.releaseDate());
        movie.setActive(request.active());
    }

    public MovieResponse toResponse(Movie movie) {
        return new MovieResponse(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getLanguage(),
                movie.getGenre(),
                movie.getDurationMinutes(),
                movie.getCertificate(),
                movie.getReleaseDate(),
                movie.isActive(),
                movie.getCreatedAt(),
                movie.getUpdatedAt());
    }
}
