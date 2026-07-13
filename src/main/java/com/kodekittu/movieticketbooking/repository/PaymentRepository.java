package com.kodekittu.movieticketbooking.repository;

import com.kodekittu.movieticketbooking.entity.Payment;
import com.kodekittu.movieticketbooking.entity.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    List<Payment> findByBookingIdOrderByCreatedAtDesc(UUID bookingId);

    Optional<Payment> findFirstByBookingIdAndStatusOrderByCreatedAtDesc(UUID bookingId, PaymentStatus status);
}
