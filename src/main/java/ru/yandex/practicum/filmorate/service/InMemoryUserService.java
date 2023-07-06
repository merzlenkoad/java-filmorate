package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.NotFoundException;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class InMemoryUserService implements UserService {

    public final UserStorage userStorage;

    @Override
    public User addUser(User user) {
        isValid(user);
        return userStorage.addUser(user);
    }

    @Override
    public User updateUser(User user) {
        isValid(user);
        checkUserExists(user.getId());
        return userStorage.updateUser(user);
    }

    @Override
    public List<User> getAll() {
        ArrayList<User> allUsers = new ArrayList<>(userStorage.getUsers().values());
        return allUsers;
    }

    @Override
    public User getUser(Integer id) {
        checkUserExists(id);
        return userStorage.getUsers().get(id);
    }

    @Override
    public void addFriend(Integer id, Integer otherUserId) {

        checkUserExists(id);
        checkUserExists(otherUserId);

        userStorage.getUsers().get(id).setFriends(otherUserId);
        userStorage.getUsers().get(otherUserId).setFriends(id);
    }

    @Override
    public void removeFriend(Integer id, Integer otherUserId) {
        checkUserExists(id);
        checkUserExists(otherUserId);

        userStorage.getUsers().get(id).getFriends().remove(otherUserId);
    }

    @Override
    public List<User> friendsList(Integer id) {
        checkUserExists(id);

        User actualUser = userStorage.getUsers().get(id);
        List<User> friendsList = new ArrayList<>();

        for (Integer key: actualUser.getFriends()) {
            friendsList.add(userStorage.getUsers().get(key));
        }
        return friendsList;
    }

    @Override
    public List<User> mutualFriendsList(Integer id, Integer otherUserId) {
        checkUserExists(id);
        checkUserExists(otherUserId);


        User actualUser = userStorage.getUsers().get(id);
        User otherUser = userStorage.getUsers().get(otherUserId);

        if (actualUser.getFriends().isEmpty() || otherUser.getFriends().isEmpty()) {
            return new ArrayList<>();
        }
        List<User> mutualFriendsList = new ArrayList<>();

        for (Integer key: actualUser.getFriends()) {
            if (otherUser.getFriends().contains(key)) {
                mutualFriendsList.add(userStorage.getUsers().get(key));
            }
        }
        return mutualFriendsList;
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

    public void checkUserExists(Integer id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new NotFoundException("Validation Exception! There is no such user with id: ", id);
        }
    }

    public User getUserById(int id) {
        return userStorage.getUsers().get(id);
    }
}
