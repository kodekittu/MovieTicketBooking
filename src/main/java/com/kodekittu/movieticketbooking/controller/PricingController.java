package com.kodekittu.movieticketbooking.controller;

import com.kodekittu.movieticketbooking.dto.common.PageResponse;
import com.kodekittu.movieticketbooking.dto.request.PriceQuoteRequest;
import com.kodekittu.movieticketbooking.dto.request.PricingTierRequest;
import com.kodekittu.movieticketbooking.dto.response.PriceQuoteResponse;
import com.kodekittu.movieticketbooking.dto.response.PricingTierResponse;
import com.kodekittu.movieticketbooking.service.PricingService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PricingController {

    private final PricingService pricingService;

    @PostMapping("/pricing-rules")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public PricingTierResponse create(@Valid @RequestBody PricingTierRequest request) {
        return pricingService.create(request);
    }

    @GetMapping("/pricing-rules")
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<PricingTierResponse> list(Pageable pageable) {
        return pricingService.list(pageable);
    }

    @PutMapping("/pricing-rules/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public PricingTierResponse update(@PathVariable UUID id, @Valid @RequestBody PricingTierRequest request) {
        return pricingService.update(id, request);
    }

    @DeleteMapping("/pricing-rules/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable UUID id) {
        pricingService.delete(id);
    }

    @PostMapping("/pricing/quote")
    public PriceQuoteResponse quote(@Valid @RequestBody PriceQuoteRequest request) {
        return pricingService.quote(request);
    }
}
