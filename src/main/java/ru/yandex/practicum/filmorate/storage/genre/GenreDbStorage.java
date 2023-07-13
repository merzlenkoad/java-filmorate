package ru.yandex.practicum.filmorate.storage.genre;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
@AllArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getGenres() {
        return jdbcTemplate.query("SELECT * FROM genres", genreRowMapper());
    }

    @Override
    public Genre getGenreById(Integer genreId) {
        if (genreId == null) {
            throw new ValidationException("ValidationException! Id is null.");
        }
        SqlRowSet genreCheck = jdbcTemplate.queryForRowSet("SELECT * FROM genres WHERE genre_id = ?", genreId);
        if (genreCheck.first()) {
            return jdbcTemplate.queryForObject("SELECT * FROM genres WHERE genre_id = ?",
                    genreRowMapper(), genreId);
        } else {
            throw new NotFoundException("There is no such genre with id: ", genreId);
        }
    }

    @Override
    public void delete(Film film) {
        jdbcTemplate.update("DELETE FROM films_genres WHERE film_id = ?", film.getId());
    }

    @Override
    public void addGenre(Film film) {
        for (Genre genre: film.getGenres()) {
            jdbcTemplate.update("INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)",
                    film.getId(), genre.getId());
        }
    }

    @Override
    public List<Genre> getFilmGenres(Integer filmId) {
        return jdbcTemplate.query("SELECT fg.genre_id, name FROM films_genres AS fg" +
                " INNER JOIN genres AS g ON fg.genre_id = g.genre_id WHERE film_id = ?", genreRowMapper(), filmId);
    }

    public RowMapper<Genre> genreRowMapper() {
        return ((rs, rowNum) -> new Genre(
                rs.getInt("genre_id"),
                rs.getString("name")));
    }
}
