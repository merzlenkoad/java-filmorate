package ru.yandex.practicum.filmorate.storage.like;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.genre.GenreService;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;

import java.util.HashSet;
import java.util.List;

@Component
@AllArgsConstructor
public class LikeDbStorage implements LikeStorage {

    private  final JdbcTemplate jdbcTemplate;
    private final MpaService mpaService;
    private final GenreService genreService;

    @Override
    public void addLike(Integer filmId, Integer userId) {
        jdbcTemplate.update("INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)",
                filmId, userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        SqlRowSet filmCheck = jdbcTemplate.queryForRowSet("SELECT * FROM film_likes WHERE film_id = ?", filmId);
        SqlRowSet likeCheck = jdbcTemplate.queryForRowSet("SELECT * FROM film_likes WHERE user_id = ?", userId);

        if (filmCheck.first() && likeCheck.first()) {
            jdbcTemplate.update("DELETE FROM film_likes WHERE film_id = ? AND user_id = ?",
                    filmId, userId);
        } else {
            throw new NotFoundException("Invalid id entered!", filmId);
        }

    }

    @Override
    public List<Film> getPopular(Integer count) {
        String sql = "SELECT id, name, description, release_date, duration, rating_id" +
                " FROM films LEFT JOIN film_likes ON films.id = film_likes.film_id" +
                " GROUP BY films.id ORDER BY COUNT(film_likes.user_id) DESC LIMIT ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new Film(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                new HashSet<>(getLikes(rs.getInt("id"))),
                mpaService.getMpaById(rs.getInt("rating_id")),
                genreService.getFilmGenres(rs.getInt("id"))),
                count);
    }

    @Override
    public List<Integer> getLikes(Integer filmId) {
        return jdbcTemplate.query("SELECT user_id FROM film_likes WHERE film_id = ?",
                (rs, rowNum) -> rs.getInt("user_id"), filmId);
    }
}
