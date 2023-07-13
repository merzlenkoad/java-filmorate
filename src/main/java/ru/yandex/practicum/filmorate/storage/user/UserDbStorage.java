package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("userDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        Map<String,Object> values = new HashMap<>();


        Number userId = simpleJdbcInsert.executeAndReturnKey(user.toMap());
        user.setId(userId.intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {

        if (getUserById(user.getId()) != null) {
            jdbcTemplate.update("UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?",
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId());
            return user;
        } else {
            throw new NotFoundException("There is no such user with id: ", user.getId());
        }
    }

    @Override
    public List<User> getUsers() {
        return jdbcTemplate.query("SELECT * FROM users", userRowMapper());
    }

    @Override
    public User getUserById(Integer userId) {
        if (userId == null) {
            throw new ValidationException("ValidationException! Id is null.");
        }
        SqlRowSet userCheck = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE user_id = ?", userId);
        if (userCheck.first()) {
            return jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = ?", userRowMapper(), userId);
        } else {
            throw new NotFoundException("There is no such user with id: ", userId);
        }
    }

    @Override
    public User deleteUserById(Integer userId) {
        User user = getUserById(userId);
        jdbcTemplate.update("DELETE FROM users WHERE user_id = ?", userId);
        return user;
    }

    public RowMapper<User> userRowMapper() {
        return ((rs, rowNum) -> new User(
                rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate()));
    }
}
