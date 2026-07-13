package com.kodekittu.movieticketbooking.service;

import com.kodekittu.movieticketbooking.dto.request.BulkSeatRequest;
import com.kodekittu.movieticketbooking.dto.request.SeatRequest;
import com.kodekittu.movieticketbooking.dto.response.SeatResponse;
import com.kodekittu.movieticketbooking.entity.Screen;
import com.kodekittu.movieticketbooking.entity.Seat;
import com.kodekittu.movieticketbooking.exception.ResourceNotFoundException;
import com.kodekittu.movieticketbooking.mapper.SeatMapper;
import com.kodekittu.movieticketbooking.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final ReferenceDataService referenceDataService;
    private final SeatMapper seatMapper;

    @Transactional
    public List<SeatResponse> createSeats(UUID screenId, BulkSeatRequest request) {
        Screen screen = referenceDataService.screen(screenId);
        List<Seat> seats = request.seats().stream()
                .map(seatRequest -> seatMapper.toEntity(seatRequest, screen))
                .toList();
        return seatRepository.saveAll(seats).stream()
                .map(seatMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SeatResponse> listByScreen(UUID screenId) {
        return seatRepository.findByScreenIdAndActiveTrueOrderByRowLabelAscSeatNumberAsc(screenId).stream()
                .map(seatMapper::toResponse)
                .toList();
    }

    @Transactional
    public SeatResponse update(UUID seatId, SeatRequest request) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));
        seatMapper.update(seat, request);
        return seatMapper.toResponse(seat);
    }

    @Transactional
    public void delete(UUID id) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));
        seat.setActive(false);
    }
}
