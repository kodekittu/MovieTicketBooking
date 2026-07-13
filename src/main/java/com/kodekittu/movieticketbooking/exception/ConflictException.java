package com.kodekittu.movieticketbooking.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends BusinessException {

    public ConflictException(ErrorCode errorCode, String message) {
        super(errorCode, HttpStatus.CONFLICT, message);
    }

    public ConflictException(String message) {
        this(ErrorCode.CONFLICT, message);
    }
}
