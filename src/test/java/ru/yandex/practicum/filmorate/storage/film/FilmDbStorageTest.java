package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureCache
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final FilmStorage filmStorage;
    private Film firstFilm;
    private Film secondFilm;

    @BeforeEach
    public void setUp() {
        firstFilm = Film.builder()
                .name("first film")
                .description("description FirstFilm")
                .releaseDate(LocalDate.of(1990, 10, 10))
                .duration(150)
                .mpa(new Mpa(1,"G"))
                .genres(new HashSet<>())
                .likes(new HashSet<>())
                .build();

        secondFilm = Film.builder()
                .name("second film")
                .description("description SecondFilm")
                .releaseDate(LocalDate.of(1990, 11, 11))
                .duration(150)
                .mpa(new Mpa(1,"G"))
                .genres(new HashSet<>())
                .likes(new HashSet<>())
                .build();
    }

    @Test
    void getFilmsAndCreate() {
        firstFilm = filmStorage.create(firstFilm);
        secondFilm = filmStorage.create(secondFilm);
        List<Film> expectedFilms = filmStorage.getFilms();

        assertThat(expectedFilms).contains(firstFilm).contains(secondFilm);
    }


    @Test
    void update() {
        firstFilm = filmStorage.create(firstFilm);
        Film updateFilm = Film.builder()
                .id(firstFilm.getId())
                .name("updateName")
                .description("updateDescription")
                .releaseDate(LocalDate.of(1995, 10, 10))
                .duration(110)
                .mpa(new Mpa(1, "G"))
                .genres(new HashSet<>())
                .likes(new HashSet<>())
                .build();
        Optional<Film> testUpdateFilm = Optional.ofNullable(filmStorage.update(updateFilm));
        assertThat(testUpdateFilm)
                .hasValueSatisfying(film -> assertThat(film)
                        .hasFieldOrPropertyWithValue("name", "updateName")
                        .hasFieldOrPropertyWithValue("description", "updateDescription"));

    }

    @Test
    void getFilmById() {
        firstFilm = filmStorage.create(firstFilm);
        Optional<Film> testGetFilmById = Optional.ofNullable(filmStorage.getFilmById(firstFilm.getId()));
        assertThat(testGetFilmById)
                .hasValueSatisfying(film -> assertThat(film)
                        .hasFieldOrPropertyWithValue("id", firstFilm.getId())
                        .hasFieldOrPropertyWithValue("name", "first film"));
    }

    @Test
    void delete() {
        firstFilm = filmStorage.create(firstFilm);
        filmStorage.delete(firstFilm.getId());

        List<Film> expectedFilms = filmStorage.getFilms();
        assertThat(expectedFilms).hasSize(0);
    }
}