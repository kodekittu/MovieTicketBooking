package com.kodekittu.movieticketbooking.mapper;

import com.kodekittu.movieticketbooking.dto.request.CityRequest;
import com.kodekittu.movieticketbooking.dto.response.CityResponse;
import com.kodekittu.movieticketbooking.entity.City;
import org.springframework.stereotype.Component;

@Component
public class CityMapper {

    public City toEntity(CityRequest request) {
        City city = new City();
        update(city, request);
        return city;
    }

    public void update(City city, CityRequest request) {
        city.setName(request.name());
        city.setState(request.state());
        city.setCountry(request.country());
        city.setActive(request.active());
    }

    public CityResponse toResponse(City city) {
        return new CityResponse(
                city.getId(),
                city.getName(),
                city.getState(),
                city.getCountry(),
                city.isActive(),
                city.getCreatedAt(),
                city.getUpdatedAt());
    }
}
