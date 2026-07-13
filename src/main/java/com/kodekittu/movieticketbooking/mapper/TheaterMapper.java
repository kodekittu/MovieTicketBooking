package com.kodekittu.movieticketbooking.mapper;

import com.kodekittu.movieticketbooking.dto.request.TheaterRequest;
import com.kodekittu.movieticketbooking.dto.response.TheaterResponse;
import com.kodekittu.movieticketbooking.entity.City;
import com.kodekittu.movieticketbooking.entity.Theater;
import org.springframework.stereotype.Component;

@Component
public class TheaterMapper {

    public Theater toEntity(TheaterRequest request, City city) {
        Theater theater = new Theater();
        theater.setCity(city);
        update(theater, request, city);
        return theater;
    }

    public void update(Theater theater, TheaterRequest request, City city) {
        theater.setCity(city);
        theater.setName(request.name());
        theater.setAddress(request.address());
        theater.setActive(request.active());
    }

    public TheaterResponse toResponse(Theater theater) {
        City city = theater.getCity();
        return new TheaterResponse(
                theater.getId(),
                city.getId(),
                city.getName(),
                theater.getName(),
                theater.getAddress(),
                theater.isActive(),
                theater.getCreatedAt(),
                theater.getUpdatedAt());
    }
}
