package com.kodekittu.movieticketbooking.service;

import com.kodekittu.movieticketbooking.dto.common.PageResponse;
import com.kodekittu.movieticketbooking.dto.request.TheaterRequest;
import com.kodekittu.movieticketbooking.dto.response.TheaterResponse;
import com.kodekittu.movieticketbooking.entity.City;
import com.kodekittu.movieticketbooking.entity.Theater;
import com.kodekittu.movieticketbooking.mapper.TheaterMapper;
import com.kodekittu.movieticketbooking.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TheaterService {

    private final TheaterRepository theaterRepository;
    private final ReferenceDataService referenceDataService;
    private final TheaterMapper theaterMapper;

    @Transactional
    public TheaterResponse create(TheaterRequest request) {
        City city = referenceDataService.city(request.cityId());
        return theaterMapper.toResponse(theaterRepository.save(theaterMapper.toEntity(request, city)));
    }

    @Transactional(readOnly = true)
    public PageResponse<TheaterResponse> search(UUID cityId, String name, Pageable pageable) {
        var page = cityId == null
                ? theaterRepository.findByActiveTrue(pageable)
                : StringUtils.hasText(name)
                ? theaterRepository.findByCityIdAndActiveTrueAndNameContainingIgnoreCase(cityId, name, pageable)
                : theaterRepository.findByCityIdAndActiveTrue(cityId, pageable);
        return PageResponse.from(page.map(theaterMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public TheaterResponse get(UUID id) {
        return theaterMapper.toResponse(referenceDataService.theater(id));
    }

    @Transactional
    public TheaterResponse update(UUID id, TheaterRequest request) {
        Theater theater = referenceDataService.theater(id);
        City city = referenceDataService.city(request.cityId());
        theaterMapper.update(theater, request, city);
        return theaterMapper.toResponse(theater);
    }

    @Transactional
    public void delete(UUID id) {
        Theater theater = referenceDataService.theater(id);
        theater.setActive(false);
    }
}
