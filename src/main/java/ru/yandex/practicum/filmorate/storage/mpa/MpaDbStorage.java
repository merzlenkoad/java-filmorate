package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
@AllArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAllMpa() {
        return jdbcTemplate.query(("SELECT * FROM ratings_mpa"), mpaRowMapper());
    }

    @Override
    public Mpa getMpaById(Integer id) {
        if (id == null) {
            throw new ValidationException("ValidationException! Id is null.");
        }
        SqlRowSet mpaCheck = jdbcTemplate.queryForRowSet("SELECT * FROM ratings_mpa WHERE rating_id = ?", id);
        if (mpaCheck.first()) {
            return jdbcTemplate.queryForObject("SELECT * FROM ratings_mpa WHERE rating_id = ?", mpaRowMapper(), id);
        } else {
            throw new NotFoundException("There is no such mpa with id: ", id);
        }
    }

    public RowMapper<Mpa> mpaRowMapper() {
        return ((rs, rowNum) -> new Mpa(
                rs.getInt("rating_id"),
                rs.getString("name")));
    }
}
