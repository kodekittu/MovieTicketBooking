package com.kodekittu.movieticketbooking;

import com.kodekittu.movieticketbooking.constant.SecurityConstants;
import com.kodekittu.movieticketbooking.dto.request.SeatHoldRequest;
import com.kodekittu.movieticketbooking.entity.City;
import com.kodekittu.movieticketbooking.entity.Movie;
import com.kodekittu.movieticketbooking.entity.Role;
import com.kodekittu.movieticketbooking.entity.Screen;
import com.kodekittu.movieticketbooking.entity.Seat;
import com.kodekittu.movieticketbooking.entity.Show;
import com.kodekittu.movieticketbooking.entity.Theater;
import com.kodekittu.movieticketbooking.entity.User;
import com.kodekittu.movieticketbooking.entity.enums.SeatStatus;
import com.kodekittu.movieticketbooking.entity.enums.SeatType;
import com.kodekittu.movieticketbooking.repository.CityRepository;
import com.kodekittu.movieticketbooking.repository.MovieRepository;
import com.kodekittu.movieticketbooking.repository.RoleRepository;
import com.kodekittu.movieticketbooking.repository.ScreenRepository;
import com.kodekittu.movieticketbooking.repository.SeatRepository;
import com.kodekittu.movieticketbooking.repository.ShowRepository;
import com.kodekittu.movieticketbooking.repository.ShowSeatRepository;
import com.kodekittu.movieticketbooking.repository.TheaterRepository;
import com.kodekittu.movieticketbooking.repository.UserRepository;
import com.kodekittu.movieticketbooking.security.CustomUserDetails;
import com.kodekittu.movieticketbooking.service.SeatHoldService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BookingConcurrencyIntegrationTest {

    @Autowired private SeatHoldService seatHoldService;
    @Autowired private RoleRepository roleRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CityRepository cityRepository;
    @Autowired private MovieRepository movieRepository;
    @Autowired private TheaterRepository theaterRepository;
    @Autowired private ScreenRepository screenRepository;
    @Autowired private SeatRepository seatRepository;
    @Autowired private ShowRepository showRepository;
    @Autowired private ShowSeatRepository showSeatRepository;

    @Test
    void onlyOneUserCanHoldSameSeatConcurrently() throws Exception {
        Role role = roleRepository.findByName(SecurityConstants.ROLE_CUSTOMER).orElseThrow();
        User userOne = user("user-one@example.com", role);
        User userTwo = user("user-two@example.com", role);
        Show show = showWithOneSeat();
        UUID seatId = seatRepository.findByScreenIdAndActiveTrueOrderByRowLabelAscSeatNumberAsc(show.getScreen().getId())
                .getFirst()
                .getId();

        Callable<Boolean> firstAttempt = () -> holdSeatAs(userOne, show.getId(), seatId);
        Callable<Boolean> secondAttempt = () -> holdSeatAs(userTwo, show.getId(), seatId);

        try (var executor = Executors.newFixedThreadPool(2)) {
            List<Boolean> results = executor.invokeAll(List.of(firstAttempt, secondAttempt)).stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (Exception exception) {
                            return false;
                        }
                    })
                    .toList();

            assertThat(results).containsExactlyInAnyOrder(true, false);
        }

        assertThat(showSeatRepository.countByShowIdAndStatus(show.getId(), SeatStatus.HELD)).isEqualTo(1);
    }

    private boolean holdSeatAs(User user, UUID showId, UUID seatId) {
        try {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                    new CustomUserDetails(user),
                    null,
                    new CustomUserDetails(user).getAuthorities()));
            seatHoldService.createHold(new SeatHoldRequest(showId, List.of(seatId)));
            return true;
        } catch (RuntimeException exception) {
            return false;
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private User user(String email, Role role) {
        User user = new User();
        user.setName(email);
        user.setEmail(email);
        user.setPasswordHash("password");
        user.getRoles().add(role);
        return userRepository.save(user);
    }

    private Show showWithOneSeat() {
        City city = new City();
        city.setName("Bengaluru-" + UUID.randomUUID());
        city.setCountry("India");
        city = cityRepository.save(city);

        Theater theater = new Theater();
        theater.setCity(city);
        theater.setName("Main Theater-" + UUID.randomUUID());
        theater.setAddress("Address");
        theater = theaterRepository.save(theater);

        Screen screen = new Screen();
        screen.setTheater(theater);
        screen.setName("Screen 1-" + UUID.randomUUID());
        screen = screenRepository.save(screen);

        Seat seat = new Seat();
        seat.setScreen(screen);
        seat.setRowLabel("A");
        seat.setSeatNumber(1);
        seat.setSeatType(SeatType.REGULAR);
        seatRepository.save(seat);

        Movie movie = new Movie();
        movie.setTitle("Movie-" + UUID.randomUUID());
        movie.setLanguage("English");
        movie.setDurationMinutes(120);
        movie.setReleaseDate(LocalDate.now());
        movie = movieRepository.save(movie);

        Show show = new Show();
        show.setMovie(movie);
        show.setScreen(screen);
        show.setStartTime(Instant.now().plusSeconds(3600));
        show.setEndTime(Instant.now().plusSeconds(7200));
        show = showRepository.save(show);

        com.kodekittu.movieticketbooking.entity.ShowSeat showSeat = new com.kodekittu.movieticketbooking.entity.ShowSeat();
        showSeat.setShow(show);
        showSeat.setSeat(seat);
        showSeatRepository.save(showSeat);
        return show;
    }
}
