package com.kodekittu.movieticketbooking.controller;

import com.kodekittu.movieticketbooking.dto.request.BulkSeatRequest;
import com.kodekittu.movieticketbooking.dto.request.SeatRequest;
import com.kodekittu.movieticketbooking.dto.response.SeatResponse;
import com.kodekittu.movieticketbooking.service.SeatService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @PostMapping("/api/v1/screens/{screenId}/seats")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public List<SeatResponse> createSeats(@PathVariable UUID screenId, @Valid @RequestBody BulkSeatRequest request) {
        return seatService.createSeats(screenId, request);
    }

    @GetMapping("/api/v1/screens/{screenId}/seats")
    public List<SeatResponse> listByScreen(@PathVariable UUID screenId) {
        return seatService.listByScreen(screenId);
    }

    @PutMapping("/api/v1/seats/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public SeatResponse update(@PathVariable UUID id, @Valid @RequestBody SeatRequest request) {
        return seatService.update(id, request);
    }

    @DeleteMapping("/api/v1/seats/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable UUID id) {
        seatService.delete(id);
    }
}
