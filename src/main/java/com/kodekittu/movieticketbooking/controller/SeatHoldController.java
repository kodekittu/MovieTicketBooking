package com.kodekittu.movieticketbooking.controller;

import com.kodekittu.movieticketbooking.dto.request.SeatHoldRequest;
import com.kodekittu.movieticketbooking.dto.response.SeatHoldResponse;
import com.kodekittu.movieticketbooking.service.SeatHoldService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/seat-holds")
public class SeatHoldController {

    private final SeatHoldService seatHoldService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SeatHoldResponse create(@Valid @RequestBody SeatHoldRequest request) {
        return seatHoldService.createHold(request);
    }

    @GetMapping("/{id}")
    public SeatHoldResponse get(@PathVariable UUID id) {
        return seatHoldService.getHold(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void release(@PathVariable UUID id) {
        seatHoldService.releaseHold(id);
    }
}
