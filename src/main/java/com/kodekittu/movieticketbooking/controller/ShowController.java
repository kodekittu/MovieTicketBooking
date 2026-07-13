package com.kodekittu.movieticketbooking.controller;

import com.kodekittu.movieticketbooking.dto.common.PageResponse;
import com.kodekittu.movieticketbooking.dto.request.ShowRequest;
import com.kodekittu.movieticketbooking.dto.request.UpdateShowStatusRequest;
import com.kodekittu.movieticketbooking.dto.response.ShowResponse;
import com.kodekittu.movieticketbooking.dto.response.ShowSeatResponse;
import com.kodekittu.movieticketbooking.service.ShowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shows")
public class ShowController {

    private final ShowService showService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ShowResponse create(@Valid @RequestBody ShowRequest request) {
        return showService.create(request);
    }

    @GetMapping
    public PageResponse<ShowResponse> search(
            @RequestParam(required = false) UUID movieId,
            @RequestParam(required = false) UUID cityId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            Pageable pageable) {
        return showService.search(movieId, cityId, from, to, pageable);
    }

    @GetMapping("/{id}")
    public ShowResponse get(@PathVariable UUID id) {
        return showService.get(id);
    }

    @GetMapping("/{id}/seats")
    public List<ShowSeatResponse> getSeats(@PathVariable UUID id) {
        return showService.getSeatAvailability(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ShowResponse update(@PathVariable UUID id, @Valid @RequestBody ShowRequest request) {
        return showService.update(id, request);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ShowResponse updateStatus(@PathVariable UUID id, @Valid @RequestBody UpdateShowStatusRequest request) {
        return showService.updateStatus(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable UUID id) {
        showService.delete(id);
    }
}
