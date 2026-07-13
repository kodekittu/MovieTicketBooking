package com.kodekittu.movieticketbooking.controller;

import com.kodekittu.movieticketbooking.dto.common.PageResponse;
import com.kodekittu.movieticketbooking.dto.request.DiscountCouponRequest;
import com.kodekittu.movieticketbooking.dto.request.ValidateDiscountRequest;
import com.kodekittu.movieticketbooking.dto.response.DiscountCouponResponse;
import com.kodekittu.movieticketbooking.dto.response.DiscountValidationResponse;
import com.kodekittu.movieticketbooking.service.DiscountService;
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
@RequestMapping("/api/v1/discount-codes")
public class DiscountController {

    private final DiscountService discountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public DiscountCouponResponse create(@Valid @RequestBody DiscountCouponRequest request) {
        return discountService.create(request);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<DiscountCouponResponse> list(Pageable pageable) {
        return discountService.list(pageable);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public DiscountCouponResponse update(@PathVariable UUID id, @Valid @RequestBody DiscountCouponRequest request) {
        return discountService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable UUID id) {
        discountService.delete(id);
    }

    @PostMapping("/validate")
    public DiscountValidationResponse validate(@Valid @RequestBody ValidateDiscountRequest request) {
        return discountService.validate(request);
    }
}
