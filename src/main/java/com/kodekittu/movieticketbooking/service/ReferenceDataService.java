package com.kodekittu.movieticketbooking.service;

import com.kodekittu.movieticketbooking.entity.City;
import com.kodekittu.movieticketbooking.entity.Movie;
import com.kodekittu.movieticketbooking.entity.Screen;
import com.kodekittu.movieticketbooking.entity.Show;
import com.kodekittu.movieticketbooking.entity.Theater;
import com.kodekittu.movieticketbooking.entity.User;
import com.kodekittu.movieticketbooking.exception.ResourceNotFoundException;
import com.kodekittu.movieticketbooking.repository.CityRepository;
import com.kodekittu.movieticketbooking.repository.MovieRepository;
import com.kodekittu.movieticketbooking.repository.ScreenRepository;
import com.kodekittu.movieticketbooking.repository.ShowRepository;
import com.kodekittu.movieticketbooking.repository.TheaterRepository;
import com.kodekittu.movieticketbooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReferenceDataService {

    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;
    private final ScreenRepository screenRepository;
    private final ShowRepository showRepository;

    @Transactional(readOnly = true)
    public User user(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public City city(UUID id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found"));
    }

    @Transactional(readOnly = true)
    public Movie movie(UUID id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
    }

    @Transactional(readOnly = true)
    public Theater theater(UUID id) {
        return theaterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found"));
    }

    @Transactional(readOnly = true)
    public Screen screen(UUID id) {
        return screenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found"));
    }

    @Transactional(readOnly = true)
    public Show show(UUID id) {
        return showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));
    }
}
