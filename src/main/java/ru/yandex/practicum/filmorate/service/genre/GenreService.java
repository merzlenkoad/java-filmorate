package ru.yandex.practicum.filmorate.service.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Set;

public interface GenreService {

    Collection<Genre> getGenres();

    Genre getGenreById(Integer id);

    Set<Genre> getFilmGenres(Integer filmId);

    void putGenres(Film film);

}
