package com.kodekittu.movieticketbooking.exception;

public class SeatUnavailableException extends ConflictException {

    public SeatUnavailableException(String message) {
        super(ErrorCode.SEAT_UNAVAILABLE, message);
    }
}
