package ru.yandex.practicum.filmorate.storage.film;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genre.GenreService;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaService mpaService;
    private final GenreService genreService;
    private final LikeStorage likeStorage;


    @Override
    public List<Film> getFilms() {
        List<Film> actualFilms = jdbcTemplate.query("SELECT * FROM films", filmRowMapper());
        return actualFilms;
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("films")
                .usingGeneratedKeyColumns("id");

        film.setMpa(mpaService.getMpaById(film.getMpa().getId()));
        Number userId = simpleJdbcInsert.executeAndReturnKey(film.toMap());
        film.setId(userId.intValue());
        if (film.getGenres() != null) {
            for (Genre genre: film.getGenres()) {
                genre.setName(genreService.getGenreById(genre.getId()).getName());
            }
            genreService.putGenres(film);

        }
        return film;
    }

    @Override
    public Film update(Film film) {
        if (film == null) {
            throw new ValidationException("ValidationException! Film is null.");
        }
        String sql = "UPDATE films SET " +
                "name = ?, description = ?, release_date = ?, duration = ?, " +
                "rating_id = ? WHERE id = ?";
        if (getFilmById(film.getId()) != null) {
            jdbcTemplate.update(sql,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
            film.getId());
            if (film.getGenres() != null) {
                Collection<Genre> sortGenres = film.getGenres().stream()
                        .sorted(Comparator.comparing(Genre::getId))
                        .collect(Collectors.toList());
                film.setGenres(new LinkedHashSet<>(sortGenres));
                for (Genre genre : film.getGenres()) {
                    genre.setName(genreService.getGenreById(genre.getId()).getName());
                }
            }
                genreService.putGenres(film);
            return film;
        } else {
            throw new NotFoundException("There is no such film with id: ", film.getId());
        }
    }

    @Override
    public Film getFilmById(Integer filmId) {
        if (filmId == null) {
            throw new ValidationException("ValidationException! Id is null.");
        }
        SqlRowSet filmCheck = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE id = ?", filmId);
        if (filmCheck.first()) {
            return jdbcTemplate.queryForObject("SELECT * FROM films WHERE id = ?", filmRowMapper(), filmId);
        } else {
            throw new NotFoundException("There is no such film with id: ", filmId);
        }
    }

    @Override
    public Integer delete(Integer filmId) {
        return jdbcTemplate.update("DELETE FROM films WHERE id = ?", filmId);
    }

    public RowMapper<Film> filmRowMapper() {
        return ((rs, rowNum) -> new Film(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                new HashSet<>(likeStorage.getLikes(rs.getInt("id"))),
                mpaService.getMpaById(rs.getInt("rating_id")),
                genreService.getFilmGenres(rs.getInt("id"))));
    }
}
