package ru.yandex.practicum.filmorate.service.genre;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {

    private GenreStorage genreStorage;

    @Override
    public Collection<Genre> getGenres() {
        return genreStorage.getGenres().stream().sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Genre getGenreById(Integer id) {
        return genreStorage.getGenreById(id);
    }

    @Override
    public Set<Genre> getFilmGenres(Integer filmId) {
        return new HashSet<>(genreStorage.getFilmGenres(filmId));
    }

    @Override
    public void putGenres(Film film) {
        genreStorage.delete(film);
        genreStorage.addGenre(film);
    }
}
