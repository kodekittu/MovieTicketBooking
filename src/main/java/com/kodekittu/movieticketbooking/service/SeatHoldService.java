package com.kodekittu.movieticketbooking.service;

import com.kodekittu.movieticketbooking.dto.request.SeatHoldRequest;
import com.kodekittu.movieticketbooking.dto.response.SeatHoldResponse;
import com.kodekittu.movieticketbooking.entity.SeatHold;
import com.kodekittu.movieticketbooking.entity.Show;
import com.kodekittu.movieticketbooking.entity.ShowSeat;
import com.kodekittu.movieticketbooking.entity.User;
import com.kodekittu.movieticketbooking.entity.enums.BookingStatus;
import com.kodekittu.movieticketbooking.entity.enums.SeatHoldStatus;
import com.kodekittu.movieticketbooking.entity.enums.SeatStatus;
import com.kodekittu.movieticketbooking.event.SeatHoldExpiredEvent;
import com.kodekittu.movieticketbooking.exception.BusinessException;
import com.kodekittu.movieticketbooking.exception.ErrorCode;
import com.kodekittu.movieticketbooking.exception.ResourceNotFoundException;
import com.kodekittu.movieticketbooking.exception.SeatUnavailableException;
import com.kodekittu.movieticketbooking.mapper.SeatHoldMapper;
import com.kodekittu.movieticketbooking.repository.BookingRepository;
import com.kodekittu.movieticketbooking.repository.SeatHoldRepository;
import com.kodekittu.movieticketbooking.repository.ShowSeatRepository;
import com.kodekittu.movieticketbooking.security.SecurityUtils;
import com.kodekittu.movieticketbooking.util.RetryExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeatHoldService {

    private final SeatHoldRepository seatHoldRepository;
    private final ShowSeatRepository showSeatRepository;
    private final BookingRepository bookingRepository;
    private final ReferenceDataService referenceDataService;
    private final SecurityUtils securityUtils;
    private final SeatHoldMapper seatHoldMapper;
    private final RetryExecutor retryExecutor;
    private final TransactionTemplate transactionTemplate;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    @Value("${app.seat-hold.expiration-minutes}")
    private long holdExpirationMinutes;

    public SeatHoldResponse createHold(SeatHoldRequest request) {
        return retryExecutor.executeOnOptimisticLock(() -> transactionTemplate.execute(status -> createHoldInTransaction(request)));
    }

    @Transactional(readOnly = true)
    public SeatHoldResponse getHold(UUID holdId) {
        UUID userId = securityUtils.currentUserId();
        SeatHold hold = seatHoldRepository.findByIdAndUserId(holdId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Seat hold not found"));
        return seatHoldMapper.toResponse(hold, showSeatRepository.findBySeatHoldId(hold.getId()), true);
    }

    public void releaseHold(UUID holdId) {
        retryExecutor.executeOnOptimisticLock(() -> transactionTemplate.execute(status -> {
            SeatHold hold = seatHoldRepository.findByIdForOptimisticUpdate(holdId)
                    .orElseThrow(() -> new ResourceNotFoundException("Seat hold not found"));
            if (!hold.getUser().getId().equals(securityUtils.currentUserId())) {
                throw new BusinessException(ErrorCode.FORBIDDEN, HttpStatus.FORBIDDEN, "Cannot release another user's hold");
            }
            releaseActiveHold(hold, SeatHoldStatus.RELEASED);
            return null;
        }));
    }

    public int expireDueHolds(int batchSize) {
        List<SeatHold> dueHolds = seatHoldRepository.findByStatusAndExpiresAtLessThanEqual(
                SeatHoldStatus.ACTIVE,
                clock.instant(),
                PageRequest.of(0, batchSize));
        int expired = 0;
        for (SeatHold hold : dueHolds) {
            if (expireHold(hold.getId())) {
                expired++;
            }
        }
        return expired;
    }

    public boolean expireHold(UUID holdId) {
        return retryExecutor.executeOnOptimisticLock(() -> transactionTemplate.execute(status -> {
            SeatHold hold = seatHoldRepository.findByIdForOptimisticUpdate(holdId)
                    .orElseThrow(() -> new ResourceNotFoundException("Seat hold not found"));
            if (hold.getStatus() != SeatHoldStatus.ACTIVE || hold.getExpiresAt().isAfter(clock.instant())) {
                return false;
            }
            releaseActiveHold(hold, SeatHoldStatus.EXPIRED);
            bookingRepository.findBySeatHoldId(hold.getId()).stream()
                    .filter(booking -> booking.getStatus() == BookingStatus.PAYMENT_PENDING)
                    .forEach(booking -> booking.setStatus(BookingStatus.PAYMENT_FAILED));
            eventPublisher.publishEvent(new SeatHoldExpiredEvent(hold.getId()));
            return true;
        }));
    }

    private SeatHoldResponse createHoldInTransaction(SeatHoldRequest request) {
        User user = referenceDataService.user(securityUtils.currentUserId());
        Show show = referenceDataService.show(request.showId());
        List<ShowSeat> seats = showSeatRepository.findForOptimisticUpdate(show.getId(), request.seatIds());
        if (seats.size() != request.seatIds().size()) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED, HttpStatus.BAD_REQUEST, "Invalid seat selection");
        }
        if (seats.stream().anyMatch(seat -> seat.getStatus() != SeatStatus.AVAILABLE)) {
            throw new SeatUnavailableException("One or more seats are not available");
        }
        SeatHold hold = new SeatHold();
        hold.setUser(user);
        hold.setShow(show);
        hold.setExpiresAt(Instant.now(clock).plus(holdExpirationMinutes, ChronoUnit.MINUTES));
        SeatHold savedHold = seatHoldRepository.save(hold);
        seats.forEach(seat -> {
            seat.setStatus(SeatStatus.HELD);
            seat.setSeatHold(savedHold);
        });
        return seatHoldMapper.toResponse(savedHold, seats, true);
    }

    private void releaseActiveHold(SeatHold hold, SeatHoldStatus terminalStatus) {
        if (hold.getStatus() != SeatHoldStatus.ACTIVE) {
            return;
        }
        List<ShowSeat> seats = showSeatRepository.findHeldSeatsForConversion(hold.getId());
        seats.stream()
                .filter(seat -> seat.getStatus() == SeatStatus.HELD)
                .forEach(seat -> {
                    seat.setStatus(SeatStatus.AVAILABLE);
                    seat.setSeatHold(null);
                    seat.setBooking(null);
                });
        hold.setStatus(terminalStatus);
    }
}
