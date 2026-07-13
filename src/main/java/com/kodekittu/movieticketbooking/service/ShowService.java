package com.kodekittu.movieticketbooking.service;

import com.kodekittu.movieticketbooking.dto.common.PageResponse;
import com.kodekittu.movieticketbooking.dto.request.ShowRequest;
import com.kodekittu.movieticketbooking.dto.request.UpdateShowStatusRequest;
import com.kodekittu.movieticketbooking.dto.response.ShowResponse;
import com.kodekittu.movieticketbooking.dto.response.ShowSeatResponse;
import com.kodekittu.movieticketbooking.entity.Movie;
import com.kodekittu.movieticketbooking.entity.Screen;
import com.kodekittu.movieticketbooking.entity.Show;
import com.kodekittu.movieticketbooking.entity.ShowSeat;
import com.kodekittu.movieticketbooking.exception.BusinessException;
import com.kodekittu.movieticketbooking.exception.ErrorCode;
import com.kodekittu.movieticketbooking.mapper.ShowMapper;
import com.kodekittu.movieticketbooking.mapper.ShowSeatMapper;
import com.kodekittu.movieticketbooking.repository.SeatRepository;
import com.kodekittu.movieticketbooking.repository.ShowRepository;
import com.kodekittu.movieticketbooking.repository.ShowSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShowService {

    private final ShowRepository showRepository;
    private final ShowSeatRepository showSeatRepository;
    private final SeatRepository seatRepository;
    private final ReferenceDataService referenceDataService;
    private final ShowMapper showMapper;
    private final ShowSeatMapper showSeatMapper;

    @Transactional
    public ShowResponse create(ShowRequest request) {
        validateShowTimes(request);
        Movie movie = referenceDataService.movie(request.movieId());
        Screen screen = referenceDataService.screen(request.screenId());
        if (!showRepository.findOverlappingShows(screen.getId(), request.startTime(), request.endTime()).isEmpty()) {
            throw new BusinessException(ErrorCode.CONFLICT, HttpStatus.CONFLICT, "Show overlaps with an existing show on this screen");
        }
        Show show = showRepository.save(showMapper.toEntity(request, movie, screen));
        createShowSeats(show);
        return showMapper.toResponse(show);
    }

    @Transactional(readOnly = true)
    public PageResponse<ShowResponse> search(UUID movieId, UUID cityId, Instant from, Instant to, Pageable pageable) {
        Instant start = from == null ? Instant.now() : from;
        Instant end = to == null ? start.plusSeconds(7 * 24 * 60 * 60L) : to;
        return PageResponse.from(showRepository.searchShows(movieId, cityId, start, end, pageable).map(showMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public ShowResponse get(UUID id) {
        return showMapper.toResponse(referenceDataService.show(id));
    }

    @Transactional(readOnly = true)
    public java.util.List<ShowSeatResponse> getSeatAvailability(UUID showId) {
        return showSeatRepository.findByShowIdOrderBySeatRowLabelAscSeatSeatNumberAsc(showId).stream()
                .map(showSeatMapper::toResponse)
                .toList();
    }

    @Transactional
    public ShowResponse updateStatus(UUID id, UpdateShowStatusRequest request) {
        Show show = referenceDataService.show(id);
        show.setStatus(request.status());
        return showMapper.toResponse(show);
    }

    @Transactional
    public ShowResponse update(UUID id, ShowRequest request) {
        validateShowTimes(request);
        Show show = referenceDataService.show(id);
        Movie movie = referenceDataService.movie(request.movieId());
        Screen screen = referenceDataService.screen(request.screenId());
        showMapper.update(show, request, movie, screen);
        return showMapper.toResponse(show);
    }

    @Transactional
    public void delete(UUID id) {
        Show show = referenceDataService.show(id);
        show.setStatus(com.kodekittu.movieticketbooking.entity.enums.ShowStatus.CANCELLED);
    }

    private void createShowSeats(Show show) {
        var showSeats = seatRepository.findByScreenIdAndActiveTrueOrderByRowLabelAscSeatNumberAsc(show.getScreen().getId()).stream()
                .map(seat -> {
                    ShowSeat showSeat = new ShowSeat();
                    showSeat.setShow(show);
                    showSeat.setSeat(seat);
                    return showSeat;
                })
                .toList();
        showSeatRepository.saveAll(showSeats);
    }

    private void validateShowTimes(ShowRequest request) {
        if (!request.endTime().isAfter(request.startTime())) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED, HttpStatus.BAD_REQUEST, "Show end time must be after start time");
        }
    }
}
