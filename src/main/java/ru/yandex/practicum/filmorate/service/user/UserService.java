package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    List<User> getUsers();

    User getUserById(Integer id);

    List<User> getFriends(Integer id);

    List<User> getCommonFriends(Integer id, Integer otherId);

    void addFriend(Integer id, Integer friendId);

    void deleteFriend(Integer id, Integer friendId);

    User create(User user);

    User update(User user);

    User delete(Integer id);

}
