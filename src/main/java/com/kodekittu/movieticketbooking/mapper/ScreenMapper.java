package com.kodekittu.movieticketbooking.mapper;

import com.kodekittu.movieticketbooking.dto.request.ScreenRequest;
import com.kodekittu.movieticketbooking.dto.response.ScreenResponse;
import com.kodekittu.movieticketbooking.entity.Screen;
import com.kodekittu.movieticketbooking.entity.Theater;
import org.springframework.stereotype.Component;

@Component
public class ScreenMapper {

    public Screen toEntity(ScreenRequest request, Theater theater) {
        Screen screen = new Screen();
        update(screen, request, theater);
        return screen;
    }

    public void update(Screen screen, ScreenRequest request, Theater theater) {
        screen.setTheater(theater);
        screen.setName(request.name());
        screen.setScreenType(request.screenType());
        screen.setActive(request.active());
    }

    public ScreenResponse toResponse(Screen screen) {
        Theater theater = screen.getTheater();
        return new ScreenResponse(
                screen.getId(),
                theater.getId(),
                theater.getName(),
                screen.getName(),
                screen.getScreenType(),
                screen.isActive(),
                screen.getCreatedAt(),
                screen.getUpdatedAt());
    }
}
