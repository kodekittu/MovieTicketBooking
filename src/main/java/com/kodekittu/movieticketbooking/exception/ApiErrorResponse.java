package com.kodekittu.movieticketbooking.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.util.List;

public record ApiErrorResponse(
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Instant timestamp,
        int status,
        ErrorCode errorCode,
        String message,
        String path,
        List<FieldErrorResponse> details
) {
    public static ApiErrorResponse of(int status, ErrorCode errorCode, String message, String path) {
        return new ApiErrorResponse(Instant.now(), status, errorCode, message, path, List.of());
    }

    public static ApiErrorResponse of(
            int status,
            ErrorCode errorCode,
            String message,
            String path,
            List<FieldErrorResponse> details) {
        return new ApiErrorResponse(Instant.now(), status, errorCode, message, path, details);
    }
}
