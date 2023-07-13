package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureCache
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {
    private final GenreDbStorage genreDbStorage;
    private Film film;

    @BeforeEach
    public void setUp() {
        film = Film.builder()
                .name("first film")
                .description("description FirstFilm")
                .releaseDate(LocalDate.of(1990, 10, 10))
                .duration(150)
                .mpa(new Mpa(1,"G"))
                .genres(new HashSet<>())
                .likes(new HashSet<>())
                .build();
    }

    @Test
    void getGenres() {
        List<Genre> exceptedGenreList = new ArrayList<>();
        exceptedGenreList.add(new Genre(1, "Комедия"));
        exceptedGenreList.add(new Genre(2, "Драма"));
        exceptedGenreList.add(new Genre(3, "Мультфильм"));
        exceptedGenreList.add(new Genre(4, "Триллер"));
        exceptedGenreList.add(new Genre(5, "Документальный"));
        exceptedGenreList.add(new Genre(6, "Боевик"));

        List<Genre> actualGenreList = genreDbStorage.getGenres();

        assertThat(exceptedGenreList).containsAll(actualGenreList);
    }

    @Test
    void getGenreById() {
        assertThat(genreDbStorage.getGenreById(1)).isEqualTo(new Genre(1, "Комедия"));
    }

    @Test
    void deleteAndGetFilmGenres() {
        genreDbStorage.delete(film);

        assertThat(genreDbStorage.getFilmGenres(film.getId())).hasSize(0);
    }
}