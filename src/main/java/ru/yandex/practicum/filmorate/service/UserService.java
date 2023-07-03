package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    User addUser(User user);
    User updateUser(User user);
    List<User> getAll();
    User getUser(Integer id);
    void addFriend(Integer id, Integer otherUserId);
    void removeFriend(Integer id, Integer otherUserId);
    List<User> friendsList(Integer id);
    List<User> mutualFriendsList(Integer id, Integer otherUserId);

}
