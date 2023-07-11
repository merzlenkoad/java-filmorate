package ru.yandex.practicum.filmorate.storage.friend;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;

import java.util.List;

@Component
@AllArgsConstructor
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(Integer userId, Integer friendId) {

        SqlRowSet userIdCheck = jdbcTemplate.queryForRowSet("SELECT * FROM users" +
                " WHERE user_id = ?", userId);
        SqlRowSet friendIdCheck = jdbcTemplate.queryForRowSet("SELECT * FROM users" +
                " WHERE user_id = ?", friendId);

        if (userIdCheck.first() && friendIdCheck.first()) {
            jdbcTemplate.update("INSERT INTO friends VALUES (?, ?)", userId, friendId);
        } else {
            throw new NotFoundException("No such friends have been found", userId);
        }

    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {

        jdbcTemplate.update("DELETE FROM friends WHERE user_id = ? AND friend_id = ?",
                userId, friendId);
    }

    @Override
    public List<Integer> getFriends(Integer userId) {
        String sqlGetFriends = "SELECT friend_id FROM friends WHERE user_id = ?";
        List<Integer> idList = jdbcTemplate.queryForList(sqlGetFriends, Integer.class, userId);
        return idList;
    }
}
