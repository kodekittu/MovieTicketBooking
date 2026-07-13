package com.kodekittu.movieticketbooking.exception;

public class HoldExpiredException extends ConflictException {

    public HoldExpiredException(String message) {
        super(ErrorCode.HOLD_EXPIRED, message);
    }
}
