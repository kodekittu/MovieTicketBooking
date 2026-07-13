package com.kodekittu.movieticketbooking.controller;

import com.kodekittu.movieticketbooking.dto.request.PaymentConfirmationRequest;
import com.kodekittu.movieticketbooking.dto.request.PaymentFailureRequest;
import com.kodekittu.movieticketbooking.dto.request.PaymentRequest;
import com.kodekittu.movieticketbooking.dto.response.BookingResponse;
import com.kodekittu.movieticketbooking.dto.response.PaymentResponse;
import com.kodekittu.movieticketbooking.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse create(@Valid @RequestBody PaymentRequest request) {
        return paymentService.createPayment(request);
    }

    @GetMapping("/{id}")
    public PaymentResponse get(@PathVariable UUID id) {
        return paymentService.getPayment(id);
    }

    @PostMapping("/{id}/confirm")
    public BookingResponse confirm(@PathVariable UUID id, @Valid @RequestBody PaymentConfirmationRequest request) {
        return paymentService.confirm(id, request);
    }

    @PostMapping("/{id}/fail")
    public BookingResponse fail(@PathVariable UUID id, @Valid @RequestBody PaymentFailureRequest request) {
        return paymentService.fail(id, request);
    }
}
