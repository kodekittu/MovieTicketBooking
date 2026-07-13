package com.kodekittu.movieticketbooking.controller;

import com.kodekittu.movieticketbooking.dto.request.ScreenRequest;
import com.kodekittu.movieticketbooking.dto.response.ScreenResponse;
import com.kodekittu.movieticketbooking.service.ScreenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ScreenController {

    private final ScreenService screenService;

    @PostMapping("/api/v1/screens")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ScreenResponse create(@Valid @RequestBody ScreenRequest request) {
        return screenService.create(request);
    }

    @GetMapping("/api/v1/theaters/{theaterId}/screens")
    public List<ScreenResponse> listByTheater(@PathVariable UUID theaterId) {
        return screenService.listByTheater(theaterId);
    }

    @GetMapping("/api/v1/screens/{id}")
    public ScreenResponse get(@PathVariable UUID id) {
        return screenService.get(id);
    }

    @PutMapping("/api/v1/screens/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ScreenResponse update(@PathVariable UUID id, @Valid @RequestBody ScreenRequest request) {
        return screenService.update(id, request);
    }

    @DeleteMapping("/api/v1/screens/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable UUID id) {
        screenService.delete(id);
    }
}
