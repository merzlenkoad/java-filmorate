package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {

    Film addFilm(Film film);
    Film updateFilm(Film film);
    Map<Integer, Film> getFilms();
}
