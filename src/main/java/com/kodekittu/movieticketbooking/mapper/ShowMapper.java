package com.kodekittu.movieticketbooking.mapper;

import com.kodekittu.movieticketbooking.dto.request.ShowRequest;
import com.kodekittu.movieticketbooking.dto.response.ShowResponse;
import com.kodekittu.movieticketbooking.entity.Movie;
import com.kodekittu.movieticketbooking.entity.Screen;
import com.kodekittu.movieticketbooking.entity.Show;
import com.kodekittu.movieticketbooking.entity.Theater;
import com.kodekittu.movieticketbooking.entity.enums.ShowStatus;
import org.springframework.stereotype.Component;

@Component
public class ShowMapper {

    public Show toEntity(ShowRequest request, Movie movie, Screen screen) {
        Show show = new Show();
        update(show, request, movie, screen);
        return show;
    }

    public void update(Show show, ShowRequest request, Movie movie, Screen screen) {
        show.setMovie(movie);
        show.setScreen(screen);
        show.setStartTime(request.startTime());
        show.setEndTime(request.endTime());
        show.setStatus(request.status() == null ? ShowStatus.SCHEDULED : request.status());
    }

    public ShowResponse toResponse(Show show) {
        Screen screen = show.getScreen();
        Theater theater = screen.getTheater();
        return new ShowResponse(
                show.getId(),
                show.getMovie().getId(),
                show.getMovie().getTitle(),
                screen.getId(),
                screen.getName(),
                theater.getId(),
                theater.getName(),
                theater.getCity().getId(),
                theater.getCity().getName(),
                show.getStartTime(),
                show.getEndTime(),
                show.getStatus(),
                show.getCreatedAt(),
                show.getUpdatedAt());
    }
}
