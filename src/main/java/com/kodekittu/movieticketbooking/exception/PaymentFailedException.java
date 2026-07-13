package com.kodekittu.movieticketbooking.exception;

import org.springframework.http.HttpStatus;

public class PaymentFailedException extends BusinessException {

    public PaymentFailedException(String message) {
        super(ErrorCode.PAYMENT_FAILED, HttpStatus.UNPROCESSABLE_ENTITY, message);
    }
}
