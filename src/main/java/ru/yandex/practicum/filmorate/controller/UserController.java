package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping
    public List<User> getUsers() {
        log.info("GET - запрос: /users - получение списка пользователей");
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        log.info("GET - запрос: /users/{id} - получение пользователя по id: " + id);
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Integer id) {
        log.info("GET - запрос: /users/{id}/friends - получение списка друзей пользователя по id: " + id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("GET - запрос: /users/{id}/friends/common/{otherId} - получение общих друзей userId: " + id +
                "и otherId: " + otherId);
        return userService.getCommonFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("PUT - запрос: /users/{id}/friends/{friendId} - добавление друга. пользователь id: " + id +
                " друг id: " + friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("DELETE - запрос: /users/{id}/friends/{friendId} - удаление друга. пользователь id: " + id +
                " друг id: " + friendId);
        userService.deleteFriend(id, friendId);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("POST - запрос: /users - добавление пользователя");
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("PUT - запрос: /users - обновление пользователя");
        return userService.update(user);
    }

    @DeleteMapping("/{id}")
    public  User delete(@PathVariable Integer id) {
        log.info("DELETE - запрос: /users/{id} - удаление пользователя по id" + id);
        return userService.delete(id);
    }
}
