package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    List<Genre> getGenres();

    Genre getGenreById(Integer genreId);

    void delete(Film film);

    void addGenre(Film film);

    List<Genre> getFilmGenres(Integer filmId);

}
