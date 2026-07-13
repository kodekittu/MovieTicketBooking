package com.kodekittu.movieticketbooking.service;

import com.kodekittu.movieticketbooking.dto.common.PageResponse;
import com.kodekittu.movieticketbooking.dto.request.CityRequest;
import com.kodekittu.movieticketbooking.dto.response.CityResponse;
import com.kodekittu.movieticketbooking.entity.City;
import com.kodekittu.movieticketbooking.mapper.CityMapper;
import com.kodekittu.movieticketbooking.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;
    private final ReferenceDataService referenceDataService;
    private final CityMapper cityMapper;

    @Transactional
    public CityResponse create(CityRequest request) {
        return cityMapper.toResponse(cityRepository.save(cityMapper.toEntity(request)));
    }

    @Transactional(readOnly = true)
    public PageResponse<CityResponse> search(String name, Pageable pageable) {
        var page = StringUtils.hasText(name)
                ? cityRepository.findByActiveTrueAndNameContainingIgnoreCase(name, pageable)
                : cityRepository.findByActiveTrue(pageable);
        return PageResponse.from(page.map(cityMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public CityResponse get(UUID id) {
        return cityMapper.toResponse(referenceDataService.city(id));
    }

    @Transactional
    public CityResponse update(UUID id, CityRequest request) {
        City city = referenceDataService.city(id);
        cityMapper.update(city, request);
        return cityMapper.toResponse(city);
    }

    @Transactional
    public void delete(UUID id) {
        City city = referenceDataService.city(id);
        city.setActive(false);
    }
}
