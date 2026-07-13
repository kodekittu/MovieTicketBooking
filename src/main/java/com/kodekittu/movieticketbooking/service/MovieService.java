package com.kodekittu.movieticketbooking.service;

import com.kodekittu.movieticketbooking.dto.common.PageResponse;
import com.kodekittu.movieticketbooking.dto.request.MovieRequest;
import com.kodekittu.movieticketbooking.dto.response.MovieResponse;
import com.kodekittu.movieticketbooking.entity.Movie;
import com.kodekittu.movieticketbooking.mapper.MovieMapper;
import com.kodekittu.movieticketbooking.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final ReferenceDataService referenceDataService;
    private final MovieMapper movieMapper;

    @Transactional
    public MovieResponse create(MovieRequest request) {
        return movieMapper.toResponse(movieRepository.save(movieMapper.toEntity(request)));
    }

    @Transactional(readOnly = true)
    public PageResponse<MovieResponse> search(String title, String language, Pageable pageable) {
        var page = StringUtils.hasText(title)
                ? movieRepository.findByActiveTrueAndTitleContainingIgnoreCase(title, pageable)
                : StringUtils.hasText(language)
                ? movieRepository.findByActiveTrueAndLanguageIgnoreCase(language, pageable)
                : movieRepository.findByActiveTrue(pageable);
        return PageResponse.from(page.map(movieMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public MovieResponse get(UUID id) {
        return movieMapper.toResponse(referenceDataService.movie(id));
    }

    @Transactional
    public MovieResponse update(UUID id, MovieRequest request) {
        Movie movie = referenceDataService.movie(id);
        movieMapper.update(movie, request);
        return movieMapper.toResponse(movie);
    }

    @Transactional
    public void delete(UUID id) {
        Movie movie = referenceDataService.movie(id);
        movie.setActive(false);
    }
}
