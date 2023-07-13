package ru.yandex.practicum.filmorate.service.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserStorage userStorage;
    private FriendStorage friendStorage;


    @Override
    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    @Override
    public User getUserById(Integer id) {
        return userStorage.getUserById(id);
    }

    @Override
    public List<User> getFriends(Integer id) {
        List<User> friendList = new ArrayList<>();
        List<Integer> idList = friendStorage.getFriends(id);
        for (Integer userId: idList) {
            friendList.add(userStorage.getUserById(userId));
        }
        return friendList;
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {
        List<User> commonFriends = new ArrayList<>();
        List<Integer> friendList = friendStorage.getFriends(id);
        List<Integer> otherFriendList = friendStorage.getFriends(otherId);
        friendList.retainAll(otherFriendList);
        for (Integer userId: friendList) {
            commonFriends.add(userStorage.getUserById(userId));
        }
        return commonFriends;
    }

    @Override
    public void addFriend(Integer id, Integer friendId) {
        friendStorage.addFriend(id, friendId);
    }

    @Override
    public void deleteFriend(Integer id, Integer friendId) {
        friendStorage.deleteFriend(id, friendId);
    }

    @Override
    public User create(User user) {
        isValid(user);
        return userStorage.addUser(user);
    }

    @Override
    public User update(User user) {
            return userStorage.updateUser(user);
    }

    @Override
    public User delete(Integer id) {
        return userStorage.deleteUserById(id);
    }

    public void isValid(User user) {
        LocalDate currentTime = LocalDate.now();

        if ((user.getLogin().contains(" ") || user.getLogin().isBlank())
                || (user.getEmail().isBlank()) || !(user.getEmail().contains("@"))
                || (user.getBirthday().compareTo(currentTime) > 0)) {
            throw new ValidationException("Validation Exception!"
                    + "The email cannot be empty and must contain the character @;\n" +
                    "The login cannot be empty and contain spaces;\n" +
                    "The name to display can be empty — in this case, the login will be used;\n" +
                    "The date of birth cannot be in the future.");
        } else if (user.getName() == null || user.getName().isBlank()) {
            String newName = user.getLogin();
            user.setName(newName);
        }
    }
}
