package ru.yandex.practicum.filmorate.storage.friend;

import java.util.List;

public interface FriendStorage {

    void addFriend(Integer userId, Integer friendId);

    void deleteFriend(Integer userId, Integer friendId);

    List<Integer> getFriends(Integer userId);

}
