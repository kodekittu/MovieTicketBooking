package com.kodekittu.movieticketbooking.service;

import com.kodekittu.movieticketbooking.dto.common.PageResponse;
import com.kodekittu.movieticketbooking.dto.request.ScreenRequest;
import com.kodekittu.movieticketbooking.dto.response.ScreenResponse;
import com.kodekittu.movieticketbooking.entity.Screen;
import com.kodekittu.movieticketbooking.entity.Theater;
import com.kodekittu.movieticketbooking.mapper.ScreenMapper;
import com.kodekittu.movieticketbooking.repository.ScreenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScreenService {

    private final ScreenRepository screenRepository;
    private final ReferenceDataService referenceDataService;
    private final ScreenMapper screenMapper;

    @Transactional
    public ScreenResponse create(ScreenRequest request) {
        Theater theater = referenceDataService.theater(request.theaterId());
        return screenMapper.toResponse(screenRepository.save(screenMapper.toEntity(request, theater)));
    }

    @Transactional(readOnly = true)
    public List<ScreenResponse> listByTheater(UUID theaterId) {
        return screenRepository.findByTheaterIdAndActiveTrue(theaterId).stream()
                .map(screenMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PageResponse<ScreenResponse> pageByTheater(UUID theaterId, Pageable pageable) {
        return PageResponse.from(screenRepository.findByTheaterId(theaterId, pageable).map(screenMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public ScreenResponse get(UUID id) {
        return screenMapper.toResponse(referenceDataService.screen(id));
    }

    @Transactional
    public ScreenResponse update(UUID id, ScreenRequest request) {
        Screen screen = referenceDataService.screen(id);
        Theater theater = referenceDataService.theater(request.theaterId());
        screenMapper.update(screen, request, theater);
        return screenMapper.toResponse(screen);
    }

    @Transactional
    public void delete(UUID id) {
        Screen screen = referenceDataService.screen(id);
        screen.setActive(false);
    }
}
