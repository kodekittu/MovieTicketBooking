package com.kodekittu.movieticketbooking.service;

import com.kodekittu.movieticketbooking.dto.request.PaymentConfirmationRequest;
import com.kodekittu.movieticketbooking.dto.request.PaymentFailureRequest;
import com.kodekittu.movieticketbooking.dto.request.PaymentRequest;
import com.kodekittu.movieticketbooking.dto.response.BookingResponse;
import com.kodekittu.movieticketbooking.dto.response.PaymentResponse;
import com.kodekittu.movieticketbooking.entity.Booking;
import com.kodekittu.movieticketbooking.entity.Payment;
import com.kodekittu.movieticketbooking.entity.enums.BookingStatus;
import com.kodekittu.movieticketbooking.exception.BusinessException;
import com.kodekittu.movieticketbooking.exception.ErrorCode;
import com.kodekittu.movieticketbooking.exception.ResourceNotFoundException;
import com.kodekittu.movieticketbooking.mapper.PaymentMapper;
import com.kodekittu.movieticketbooking.repository.BookingRepository;
import com.kodekittu.movieticketbooking.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final BookingService bookingService;
    private final PaymentMapper paymentMapper;

    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        Booking booking = bookingRepository.findByIdForOptimisticUpdate(request.bookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        if (booking.getStatus() != BookingStatus.PAYMENT_PENDING) {
            throw new BusinessException(ErrorCode.BUSINESS_RULE_VIOLATION, HttpStatus.UNPROCESSABLE_ENTITY, "Booking is not pending payment");
        }
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(booking.getTotalAmount());
        payment.setPaymentMethod(request.paymentMethod());
        return paymentMapper.toResponse(paymentRepository.save(payment));
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPayment(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .map(paymentMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
    }

    public BookingResponse confirm(UUID paymentId, PaymentConfirmationRequest request) {
        return bookingService.confirmPayment(paymentId, request.providerReference());
    }

    public BookingResponse fail(UUID paymentId, PaymentFailureRequest request) {
        return bookingService.failPayment(paymentId, request.failureReason());
    }
}
