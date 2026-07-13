package com.kodekittu.movieticketbooking.repository;

import com.kodekittu.movieticketbooking.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MovieRepository extends JpaRepository<Movie, UUID> {

    Page<Movie> findByActiveTrue(Pageable pageable);

    Page<Movie> findByActiveTrueAndTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Movie> findByActiveTrueAndLanguageIgnoreCase(String language, Pageable pageable);
}
