package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    List<Film> getFilms();

    Film getFilmById(Integer id);

    List<Film> getPopular(Integer count);

    Film create(Film film);

    Film update(Film film);

    void addLike(Integer id, Integer userId);

    void deleteLike(Integer id, Integer userId);

    Integer delete(Integer id);
}
