package com.kodekittu.movieticketbooking.testData;

import com.kodekittu.movieticketbooking.ai.service.MovieEmbeddingService;
import com.kodekittu.movieticketbooking.dto.request.*;
import com.kodekittu.movieticketbooking.entity.Movie;
import com.kodekittu.movieticketbooking.entity.Screen;
import com.kodekittu.movieticketbooking.entity.Theater;
import com.kodekittu.movieticketbooking.entity.enums.SeatType;
import com.kodekittu.movieticketbooking.entity.enums.ShowStatus;
import com.kodekittu.movieticketbooking.repository.*;
import com.kodekittu.movieticketbooking.service.MovieService;
import com.kodekittu.movieticketbooking.service.ScreenService;
import com.kodekittu.movieticketbooking.service.SeatService;
import com.kodekittu.movieticketbooking.service.ShowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = "app.seed-data",
        havingValue = "true"
)
@Profile("!test")
public class DemoDataSeeder implements CommandLineRunner {

    private final TheaterRepository theaterRepository;
    private final ScreenRepository screenRepository;
    private final SeatRepository seatRepository;
    private final MovieRepository movieRepository;
    private final ShowRepository showRepository;
    private final MovieEmbeddingService movieEmbeddingService;
    private final ScreenService screenService;
    private final SeatService seatService;
    private final MovieService movieService;
    private final ShowService showService;

    @Override
    @Transactional
    public void run(String... args) {

        seedScreens();

        seedSeats();

        seedMovies();

        seedShows();

        indexMovies();

        log.info("Demo data seeded successfully.");

    }

    private void seedSeats() {

        if (seatRepository.count() > 0) {
            log.info("Seats already exist.");
            return;
        }

        List<Screen> screens = screenRepository.findAll();

        for (Screen screen : screens) {

            List<SeatRequest> seats = new ArrayList<>();

            for (char row = 'A'; row <= 'J'; row++) {

                for (int number = 1; number <= 10; number++) {

                    SeatType seatType;

                    if (row <= 'C') {
                        seatType = SeatType.PREMIUM;
                    } else if (row <= 'F') {
                        seatType = SeatType.RECLINER;
                    } else {
                        seatType = SeatType.REGULAR;
                    }

                    seats.add(
                            new SeatRequest(
                                    String.valueOf(row),
                                    number,
                                    seatType,
                                    true
                            )
                    );

                }

            }

            seatService.createSeats(
                    screen.getId(),
                    new BulkSeatRequest(seats)
            );

        }

        log.info("Seats Seeded : {}", seatRepository.count());

    }

    private void seedMovies() {

        if (movieRepository.count() > 0) {
            log.info("Movies already exist.");
            return;
        }

        List<MovieRequest> movies = List.of(

                new MovieRequest(
                        "Interstellar",
                        "A team of astronauts travels through a wormhole to find a new home for humanity.",
                        "English",
                        "Sci-Fi",
                        169,
                        "UA",
                        LocalDate.of(2014, 11, 7),
                        true
                ),

                new MovieRequest(
                        "Inception",
                        "A skilled thief steals secrets through dream-sharing technology.",
                        "English",
                        "Sci-Fi",
                        148,
                        "UA",
                        LocalDate.of(2010, 7, 16),
                        true
                ),

                new MovieRequest(
                        "The Dark Knight",
                        "Batman faces the Joker in Gotham City.",
                        "English",
                        "Action",
                        152,
                        "UA",
                        LocalDate.of(2008, 7, 18),
                        true
                ),

                new MovieRequest(
                        "Oppenheimer",
                        "The story of physicist J. Robert Oppenheimer and the Manhattan Project.",
                        "English",
                        "Biography",
                        180,
                        "A",
                        LocalDate.of(2023, 7, 21),
                        true
                ),

                new MovieRequest(
                        "Dune",
                        "A young nobleman leads the fight for the desert planet Arrakis.",
                        "English",
                        "Sci-Fi",
                        155,
                        "UA",
                        LocalDate.of(2021, 10, 22),
                        true
                ),

                new MovieRequest(
                        "Avatar",
                        "A marine discovers the world of Pandora.",
                        "English",
                        "Sci-Fi",
                        162,
                        "UA",
                        LocalDate.of(2009, 12, 18),
                        true
                ),

                new MovieRequest(
                        "The Martian",
                        "An astronaut struggles to survive alone on Mars.",
                        "English",
                        "Sci-Fi",
                        144,
                        "UA",
                        LocalDate.of(2015, 10, 2),
                        true
                ),

                new MovieRequest(
                        "Gravity",
                        "Two astronauts fight to survive after a disaster in space.",
                        "English",
                        "Sci-Fi",
                        91,
                        "UA",
                        LocalDate.of(2013, 10, 4),
                        true
                ),

                new MovieRequest(
                        "Top Gun Maverick",
                        "Pete Maverick Mitchell returns to train elite fighter pilots.",
                        "English",
                        "Action",
                        131,
                        "UA",
                        LocalDate.of(2022, 5, 27),
                        true
                ),

                new MovieRequest(
                        "John Wick",
                        "An ex-hitman seeks revenge after losing everything.",
                        "English",
                        "Action",
                        101,
                        "A",
                        LocalDate.of(2014, 10, 24),
                        true
                ),

                new MovieRequest(
                        "KGF Chapter 2",
                        "Rocky fights powerful enemies to become the king of the gold empire.",
                        "Kannada",
                        "Action",
                        168,
                        "UA",
                        LocalDate.of(2022, 4, 14),
                        true
                ),

                new MovieRequest(
                        "RRR",
                        "Two revolutionaries unite against British rule.",
                        "Telugu",
                        "Action",
                        187,
                        "UA",
                        LocalDate.of(2022, 3, 25),
                        true
                ),

                new MovieRequest(
                        "Baahubali 2",
                        "The conclusion of the epic Mahishmati saga.",
                        "Telugu",
                        "Action",
                        167,
                        "UA",
                        LocalDate.of(2017, 4, 28),
                        true
                ),

                new MovieRequest(
                        "Kantara",
                        "A mystical tale rooted in the traditions of coastal Karnataka.",
                        "Kannada",
                        "Thriller",
                        148,
                        "UA",
                        LocalDate.of(2022, 9, 30),
                        true
                ),

                new MovieRequest(
                        "Pushpa",
                        "The rise of a red sandalwood smuggler.",
                        "Telugu",
                        "Action",
                        179,
                        "UA",
                        LocalDate.of(2021, 12, 17),
                        true
                ),

                new MovieRequest(
                        "Dangal",
                        "A wrestler trains his daughters to become world champions.",
                        "Hindi",
                        "Sports",
                        161,
                        "UA",
                        LocalDate.of(2016, 12, 23),
                        true
                ),

                new MovieRequest(
                        "3 Idiots",
                        "Three engineering students redefine friendship and education.",
                        "Hindi",
                        "Comedy",
                        170,
                        "UA",
                        LocalDate.of(2009, 12, 25),
                        true
                ),

                new MovieRequest(
                        "PK",
                        "An alien questions human beliefs and traditions.",
                        "Hindi",
                        "Comedy",
                        153,
                        "UA",
                        LocalDate.of(2014, 12, 19),
                        true
                ),

                new MovieRequest(
                        "Drishyam",
                        "A family man protects his family after an unexpected crime.",
                        "Hindi",
                        "Thriller",
                        163,
                        "UA",
                        LocalDate.of(2015, 7, 31),
                        true
                ),

                new MovieRequest(
                        "Vikram",
                        "A retired special agent returns to eliminate a dangerous syndicate.",
                        "Tamil",
                        "Action",
                        175,
                        "UA",
                        LocalDate.of(2022, 6, 3),
                        true
                )

        );

        movies.forEach(movieService::create);

        log.info("Movies Seeded : {}", movieRepository.count());

    }

    private void seedShows() {

        if (showRepository.count() > 0) {
            log.info("Shows already exist.");
            return;
        }

        List<Movie> movies = movieRepository.findAll();
        List<Screen> screens = screenRepository.findAll();

        if (movies.isEmpty() || screens.isEmpty()) {
            log.warn("Movies or Screens are missing.");
            return;
        }

        int movieIndex = 0;

        for (Screen screen : screens) {

            for (int day = 1; day <= 5; day++) {

                Movie movie = movies.get(movieIndex % movies.size());

                Instant startTime = LocalDate.now()
                        .plusDays(day)
                        .atTime(10 + (movieIndex % 4) * 3, 0)
                        .atZone(ZoneId.systemDefault())
                        .toInstant();

                Instant endTime = startTime.plus(
                        Duration.ofMinutes(movie.getDurationMinutes())
                );

                showService.create(

                        new ShowRequest(
                                movie.getId(),
                                screen.getId(),
                                startTime,
                                endTime,
                                ShowStatus.SCHEDULED
                        )

                );

                movieIndex++;

            }

        }

        log.info("Shows Seeded : {}", showRepository.count());

    }

    private void seedScreens() {

        if (screenRepository.count() > 0) {
            log.info("Screens already exist.");
            return;
        }

        List<Theater> theaters = theaterRepository.findAll();

        for (Theater theater : theaters) {

            createScreen(theater, "Screen 1", "IMAX");
            createScreen(theater, "Screen 2", "STANDARD");

        }

        log.info("{} Screens Created", screenRepository.count());

    }

    private void createScreen(Theater theater,
                              String name,
                              String type) {

        screenService.create(

                new ScreenRequest(
                        theater.getId(),
                        name,
                        type,
                        true
                )

        );

    }

    private void indexMovies() {

        log.info("--------------------------------");
        log.info("Indexing Movies into Pinecone...");
        log.info("--------------------------------");

        movieEmbeddingService.indexAllMovies();

        log.info("--------------------------------");
        log.info("Pinecone Indexing Completed");
        log.info("--------------------------------");

    }

}