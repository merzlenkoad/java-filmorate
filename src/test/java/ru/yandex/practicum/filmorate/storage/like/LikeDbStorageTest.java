package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmServiceImpl;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

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
class LikeDbStorageTest {
    private final LikeDbStorage likeDbStorage;
    private final UserDbStorage userDbStorage;
    private final FilmDbStorage filmDbStorage;
    private final FilmServiceImpl filmService;
    private Film film;
    private User user;

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

        user = User.builder()
                .name("first user")
                .login("first login")
                .email("firstuser@email.ru")
                .birthday(LocalDate.of(1996, 12, 5))
                .build();
    }

    @Test
    void addLike() {
        user = userDbStorage.addUser(user);
        film = filmDbStorage.create(film);
        likeDbStorage.addLike(film.getId(), user.getId());
        film = filmDbStorage.getFilmById(film.getId());
        assertThat(film.getLikes()).contains(user.getId());
    }

    @Test
    void deleteLike() {
        user = userDbStorage.addUser(user);
        film = filmDbStorage.create(film);
        likeDbStorage.addLike(film.getId(), user.getId());
        likeDbStorage.deleteLike(film.getId(), user.getId());
        film = filmDbStorage.getFilmById(film.getId());
        assertThat(film.getLikes()).hasSize(0);
    }

    @Test
    void getPopular() {
        user = userDbStorage.addUser(user);
        film = filmDbStorage.create(film);
        likeDbStorage.addLike(film.getId(), user.getId());
        List<Film> filmList = filmService.getPopular(1);

        assertThat(Optional.of(filmList.get(0)))
                .hasValueSatisfying(film1 ->
                        AssertionsForClassTypes.assertThat(film1)
                                .hasFieldOrPropertyWithValue("name", "first film"));
    }
}