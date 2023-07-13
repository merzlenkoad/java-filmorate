package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.util.List;

import static ru.yandex.practicum.filmorate.controller.Constants.ACCEPT_RELEASE_DATE;

@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {
    private FilmStorage filmStorage;
    private LikeStorage likeStorage;

    @Override
    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    @Override
    public Film getFilmById(Integer id) {
        return filmStorage.getFilmById(id);
    }

    @Override
    public List<Film> getPopular(Integer count) {
        if (count < 1) {
            new ValidationException("Incorrect count!");
        }
        return likeStorage.getPopular(count);
    }

    @Override
    public Film create(Film film) {
        if (film.getReleaseDate().isAfter(ACCEPT_RELEASE_DATE)) {
            return filmStorage.create(film);
        } else {
            throw new ValidationException("Wrong date release!");
        }
    }

    @Override
    public Film update(Film film) {
        return filmStorage.update(film);
    }

    @Override
    public void addLike(Integer id, Integer userId) {
        likeStorage.addLike(id, userId);
    }

    @Override
    public void deleteLike(Integer id, Integer userId) {
        likeStorage.deleteLike(id, userId);
    }

    @Override
    public Integer delete(Integer id) {
        return filmStorage.delete(id);
    }
}
