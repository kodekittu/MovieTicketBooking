package com.kodekittu.movieticketbooking.service;

import com.kodekittu.movieticketbooking.constant.SecurityConstants;
import com.kodekittu.movieticketbooking.dto.common.PageResponse;
import com.kodekittu.movieticketbooking.dto.request.CancelBookingRequest;
import com.kodekittu.movieticketbooking.dto.request.CreateBookingRequest;
import com.kodekittu.movieticketbooking.dto.response.BookingResponse;
import com.kodekittu.movieticketbooking.dto.response.CancellationResponse;
import com.kodekittu.movieticketbooking.entity.Booking;
import com.kodekittu.movieticketbooking.entity.BookingSeat;
import com.kodekittu.movieticketbooking.entity.Payment;
import com.kodekittu.movieticketbooking.entity.SeatHold;
import com.kodekittu.movieticketbooking.entity.ShowSeat;
import com.kodekittu.movieticketbooking.entity.User;
import com.kodekittu.movieticketbooking.entity.enums.BookingStatus;
import com.kodekittu.movieticketbooking.entity.enums.PaymentStatus;
import com.kodekittu.movieticketbooking.entity.enums.SeatHoldStatus;
import com.kodekittu.movieticketbooking.entity.enums.SeatStatus;
import com.kodekittu.movieticketbooking.event.BookingCancelledEvent;
import com.kodekittu.movieticketbooking.event.BookingConfirmedEvent;
import com.kodekittu.movieticketbooking.event.PaymentFailedEvent;
import com.kodekittu.movieticketbooking.exception.BusinessException;
import com.kodekittu.movieticketbooking.exception.ErrorCode;
import com.kodekittu.movieticketbooking.exception.HoldExpiredException;
import com.kodekittu.movieticketbooking.exception.ResourceNotFoundException;
import com.kodekittu.movieticketbooking.mapper.BookingMapper;
import com.kodekittu.movieticketbooking.repository.BookingRepository;
import com.kodekittu.movieticketbooking.repository.BookingSeatRepository;
import com.kodekittu.movieticketbooking.repository.PaymentRepository;
import com.kodekittu.movieticketbooking.repository.SeatHoldRepository;
import com.kodekittu.movieticketbooking.repository.ShowSeatRepository;
import com.kodekittu.movieticketbooking.security.SecurityUtils;
import com.kodekittu.movieticketbooking.util.BookingReferenceGenerator;
import com.kodekittu.movieticketbooking.util.RetryExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.time.Clock;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final SeatHoldRepository seatHoldRepository;
    private final ShowSeatRepository showSeatRepository;
    private final PaymentRepository paymentRepository;
    private final ReferenceDataService referenceDataService;
    private final PricingService pricingService;
    private final RefundService refundService;
    private final SecurityUtils securityUtils;
    private final BookingMapper bookingMapper;
    private final BookingReferenceGenerator bookingReferenceGenerator;
    private final RetryExecutor retryExecutor;
    private final TransactionTemplate transactionTemplate;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    public BookingResponse createBooking(CreateBookingRequest request) {
        return retryExecutor.executeOnOptimisticLock(() -> transactionTemplate.execute(status -> createBookingInTransaction(request)));
    }

    @Transactional(readOnly = true)
    public BookingResponse getBooking(UUID bookingId) {
        Booking booking = bookingRepository.findWithDetailsById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        assertBookingReadable(booking);
        return bookingMapper.toResponse(booking, bookingSeatRepository.findByBookingId(booking.getId()));
    }

    @Transactional(readOnly = true)
    public PageResponse<BookingResponse> listMyBookings(Pageable pageable) {
        UUID userId = securityUtils.currentUserId();
        return PageResponse.from(bookingRepository.findByUserId(userId, pageable)
                .map(booking -> bookingMapper.toResponse(booking, bookingSeatRepository.findByBookingId(booking.getId()))));
    }

    @Transactional(readOnly = true)
    public PageResponse<BookingResponse> listBookings(Pageable pageable) {
        return PageResponse.from(bookingRepository.findAll(pageable)
                .map(booking -> bookingMapper.toResponse(booking, bookingSeatRepository.findByBookingId(booking.getId()))));
    }

    public BookingResponse confirmPayment(UUID paymentId, String providerReference) {
        return retryExecutor.executeOnOptimisticLock(() -> transactionTemplate.execute(status -> confirmPaymentInTransaction(paymentId, providerReference)));
    }

    public BookingResponse failPayment(UUID paymentId, String failureReason) {
        return retryExecutor.executeOnOptimisticLock(() -> transactionTemplate.execute(status -> {
            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
            Booking booking = payment.getBooking();
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason(failureReason);
            booking.setStatus(BookingStatus.PAYMENT_FAILED);
            eventPublisher.publishEvent(new PaymentFailedEvent(booking.getId(), failureReason));
            return bookingMapper.toResponse(booking, bookingSeatRepository.findByBookingId(booking.getId()));
        }));
    }

    public CancellationResponse cancelBooking(UUID bookingId, CancelBookingRequest request) {
        return retryExecutor.executeOnOptimisticLock(() -> transactionTemplate.execute(status -> cancelBookingInTransaction(bookingId)));
    }

    private BookingResponse createBookingInTransaction(CreateBookingRequest request) {
        User user = referenceDataService.user(securityUtils.currentUserId());
        SeatHold hold = seatHoldRepository.findByIdForOptimisticUpdate(request.seatHoldId())
                .orElseThrow(() -> new ResourceNotFoundException("Seat hold not found"));
        validateActiveHold(hold, user.getId());
        List<ShowSeat> heldSeats = showSeatRepository.findHeldSeatsForConversion(hold.getId());
        if (heldSeats.isEmpty() || heldSeats.stream().anyMatch(seat -> seat.getStatus() != SeatStatus.HELD)) {
            throw new BusinessException(ErrorCode.SEAT_UNAVAILABLE, HttpStatus.CONFLICT, "Held seats are no longer available");
        }
        PricingResult pricing = pricingService.calculate(
                hold.getShow().getId(),
                heldSeats.stream().map(seat -> seat.getSeat().getId()).toList(),
                request.discountCode(),
                heldSeats);
        heldSeats.forEach(showSeat -> showSeat.setPrice(pricing.priceFor(showSeat)));
        Booking booking = new Booking();
        booking.setBookingReference(bookingReferenceGenerator.nextReference());
        booking.setUser(user);
        booking.setShow(hold.getShow());
        booking.setSeatHold(hold);
        booking.setStatus(BookingStatus.PAYMENT_PENDING);
        booking.setSubtotalAmount(pricing.subtotalAmount());
        booking.setDiscountAmount(pricing.discountAmount());
        booking.setTaxAmount(pricing.taxAmount());
        booking.setTotalAmount(pricing.totalAmount());
        Booking saved = bookingRepository.save(booking);
        return bookingMapper.toResponse(saved, List.of());
    }

    private BookingResponse confirmPaymentInTransaction(UUID paymentId, String providerReference) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        Booking booking = bookingRepository.findByIdForOptimisticUpdate(payment.getBooking().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        if (booking.getStatus() != BookingStatus.PAYMENT_PENDING) {
            throw new BusinessException(ErrorCode.BUSINESS_RULE_VIOLATION, HttpStatus.UNPROCESSABLE_ENTITY, "Booking is not pending payment");
        }
        SeatHold hold = booking.getSeatHold();
        validateActiveHold(hold, booking.getUser().getId());
        List<ShowSeat> heldSeats = showSeatRepository.findHeldSeatsForConversion(hold.getId());
        heldSeats.forEach(showSeat -> {
            if (showSeat.getStatus() != SeatStatus.HELD) {
                throw new BusinessException(ErrorCode.SEAT_UNAVAILABLE, HttpStatus.CONFLICT, "Held seats are no longer available");
            }
            showSeat.setStatus(SeatStatus.BOOKED);
            showSeat.setBooking(booking);
        });
        List<BookingSeat> bookingSeats = heldSeats.stream()
                .map(showSeat -> toBookingSeat(booking, showSeat))
                .toList();
        bookingSeatRepository.saveAll(bookingSeats);
        hold.setStatus(SeatHoldStatus.CONVERTED);
        payment.setProviderReference(providerReference);
        payment.setStatus(PaymentStatus.SUCCESS);
        booking.setStatus(BookingStatus.CONFIRMED);
        eventPublisher.publishEvent(new BookingConfirmedEvent(booking.getId()));
        return bookingMapper.toResponse(booking, bookingSeats);
    }

    private CancellationResponse cancelBookingInTransaction(UUID bookingId) {
        Booking booking = bookingRepository.findByIdForOptimisticUpdate(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        assertBookingWritable(booking);
        if (booking.getStatus() != BookingStatus.CONFIRMED && booking.getStatus() != BookingStatus.PAYMENT_PENDING) {
            throw new BusinessException(ErrorCode.BUSINESS_RULE_VIOLATION, HttpStatus.UNPROCESSABLE_ENTITY, "Booking cannot be cancelled");
        }
        BigDecimal refundAmount = booking.getStatus() == BookingStatus.CONFIRMED
                ? refundService.calculateRefund(booking)
                : BigDecimal.ZERO;
        releaseSeatsForBooking(booking);
        booking.setCancelledAt(clock.instant());
        booking.setStatus(refundAmount.compareTo(BigDecimal.ZERO) > 0 ? BookingStatus.REFUNDED : BookingStatus.CANCELLED);
        paymentRepository.findFirstByBookingIdAndStatusOrderByCreatedAtDesc(booking.getId(), PaymentStatus.SUCCESS)
                .ifPresent(payment -> payment.setStatus(refundAmount.compareTo(BigDecimal.ZERO) > 0
                        ? PaymentStatus.REFUNDED
                        : PaymentStatus.SUCCESS));
        eventPublisher.publishEvent(new BookingCancelledEvent(booking.getId(), refundAmount));
        return new CancellationResponse(booking.getId(), booking.getBookingReference(), booking.getStatus(), refundAmount, "Booking cancelled");
    }

    private void releaseSeatsForBooking(Booking booking) {
        if (booking.getSeatHold() != null && booking.getSeatHold().getStatus() == SeatHoldStatus.ACTIVE) {
            booking.getSeatHold().setStatus(SeatHoldStatus.RELEASED);
        }
        showSeatRepository.findByBookingId(booking.getId()).forEach(showSeat -> {
            showSeat.setStatus(SeatStatus.AVAILABLE);
            showSeat.setBooking(null);
            showSeat.setSeatHold(null);
        });
        if (booking.getSeatHold() != null) {
            showSeatRepository.findBySeatHoldId(booking.getSeatHold().getId()).stream()
                    .filter(showSeat -> showSeat.getStatus() == SeatStatus.HELD)
                    .forEach(showSeat -> {
                        showSeat.setStatus(SeatStatus.AVAILABLE);
                        showSeat.setSeatHold(null);
                    });
        }
    }

    private BookingSeat toBookingSeat(Booking booking, ShowSeat showSeat) {
        BookingSeat bookingSeat = new BookingSeat();
        bookingSeat.setBooking(booking);
        bookingSeat.setShowSeat(showSeat);
        bookingSeat.setSeat(showSeat.getSeat());
        bookingSeat.setSeatLabel(showSeat.getSeat().label());
        bookingSeat.setPrice(showSeat.getPrice());
        return bookingSeat;
    }

    private void validateActiveHold(SeatHold hold, UUID expectedUserId) {
        if (!hold.getUser().getId().equals(expectedUserId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, HttpStatus.FORBIDDEN, "Seat hold does not belong to this user");
        }
        if (hold.getStatus() != SeatHoldStatus.ACTIVE) {
            throw new HoldExpiredException("Seat hold is no longer active");
        }
        if (!hold.getExpiresAt().isAfter(clock.instant())) {
            throw new HoldExpiredException("Seat hold has expired");
        }
    }

    private void assertBookingReadable(Booking booking) {
        if (!securityUtils.currentUserHasRole(SecurityConstants.ROLE_ADMIN)
                && !booking.getUser().getId().equals(securityUtils.currentUserId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, HttpStatus.FORBIDDEN, "Cannot access another user's booking");
        }
    }

    private void assertBookingWritable(Booking booking) {
        assertBookingReadable(booking);
    }
}
