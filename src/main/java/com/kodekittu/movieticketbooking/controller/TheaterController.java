package com.kodekittu.movieticketbooking.controller;

import com.kodekittu.movieticketbooking.dto.common.PageResponse;
import com.kodekittu.movieticketbooking.dto.request.TheaterRequest;
import com.kodekittu.movieticketbooking.dto.response.TheaterResponse;
import com.kodekittu.movieticketbooking.service.TheaterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/theaters")
public class TheaterController {

    private final TheaterService theaterService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public TheaterResponse create(@Valid @RequestBody TheaterRequest request) {
        return theaterService.create(request);
    }

    @GetMapping
    public PageResponse<TheaterResponse> search(
            @RequestParam(required = false) UUID cityId,
            @RequestParam(required = false) String name,
            Pageable pageable) {
        return theaterService.search(cityId, name, pageable);
    }

    @GetMapping("/{id}")
    public TheaterResponse get(@PathVariable UUID id) {
        return theaterService.get(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public TheaterResponse update(@PathVariable UUID id, @Valid @RequestBody TheaterRequest request) {
        return theaterService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable UUID id) {
        theaterService.delete(id);
    }
}
