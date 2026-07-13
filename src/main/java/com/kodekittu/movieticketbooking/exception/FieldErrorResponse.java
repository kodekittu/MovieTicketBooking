package com.kodekittu.movieticketbooking.exception;

public record FieldErrorResponse(
        String field,
        String message
) {
}
