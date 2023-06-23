package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private int generatedId = 1;
    private Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User createUser(@RequestBody User user) {
        isValid(user);
        user.setId(generatedId++);
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        isValid(user);
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Validation Exception!");
        }
        users.remove(user.getId());
        users.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public void isValid(User user) {
        LocalDate currentTime = LocalDate.now();

        if ((user.getLogin().contains(" ") || user.getLogin().isBlank())
                || (user.getEmail().isBlank()) || !(user.getEmail().contains("@"))
                || (user.getBirthday().compareTo(currentTime) > 0)) {
            throw new ValidationException("Validation Exception!");
        } else if (user.getName() == null || user.getName().isBlank()) {
            String newName = user.getLogin();
            user.setName(newName);
        }
    }

    public User getUserById(int id) {
        return users.get(id);
    }
}
