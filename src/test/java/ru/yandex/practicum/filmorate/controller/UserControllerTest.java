package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

class UserControllerTest {

    private UserController userController;

    @BeforeEach
    private void setUp() {
        userController = new UserController();
    }

    @Test
    public void createUser() {
        User user = new User("email@yandex.ru", "login", "name",
                LocalDate.of(1996,12,05));
        userController.createUser(user);

        User exceptedUser = new User(1,"email@yandex.ru", "login", "name",
                LocalDate.of(1996,12,05));

        Assertions.assertEquals(exceptedUser, userController.getUserById(1));
    }

    @Test
    public void createUserEmailEmpty() {
        User user = new User("", "login", "name",
                LocalDate.of(1996,12,05));

        final ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> userController.createUser(user));

        Assertions.assertEquals("Validation Exception!", e.getMessage());
    }

    @Test
    public void createUserLoginEmpty() {
        User user = new User("email@yandex.ru", "", "name",
                LocalDate.of(1996,12,05));

        final ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> userController.createUser(user));

        Assertions.assertEquals("Validation Exception!", e.getMessage());
    }

    @Test
    public void createUserLoginSpaceBar() {
        User user = new User("email@yandex.ru", "new Login", "name",
                LocalDate.of(1996,12,05));

        final ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> userController.createUser(user));

        Assertions.assertEquals("Validation Exception!", e.getMessage());
    }

    @Test
    public void createUserNameEmpty() {
        User user = new User("email@yandex.ru", "login", "",
                LocalDate.of(1996,12,05));
        userController.createUser(user);

        User exceptedUser = new User(1,"email@yandex.ru", "login", "login",
                LocalDate.of(1996,12,05));

        Assertions.assertEquals(exceptedUser, userController.getUserById(1));
    }

    @Test
    public void createUserBirthdayFuture() {
        User user = new User("email@yandex.ru", "login", "",
                LocalDate.of(2025,12,05));

        final ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> userController.createUser(user));

        Assertions.assertEquals("Validation Exception!", e.getMessage());
    }

    @Test
    public void updateUser() {
        User user = new User("email@yandex.ru", "login", "name",
                LocalDate.of(1996,12,05));
        userController.createUser(user);
        userController.updateUser(new User(1,"email@yandex.ru", "login", "name",
                        LocalDate.of(1990,12,05)));

        User exceptedUser = new User(1,"email@yandex.ru", "login", "name",
                LocalDate.of(1990,12,05));

        Assertions.assertEquals(exceptedUser, userController.getUserById(1));
    }

    @Test
    public void updateUserEmailEmpty() {
        User user = new User("email@yandex.ru", "login", "name",
                LocalDate.of(1996,12,05));
        userController.createUser(user);
        User updateUser = new User(1,"", "login", "name",
                LocalDate.of(1996,12,05));

        final ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> userController.updateUser(updateUser));

        Assertions.assertEquals("Validation Exception!", e.getMessage());
    }

    @Test
    public void updateUserLoginEmpty() {
        User user = new User("email@yandex.ru", "login", "name",
                LocalDate.of(1996,12,05));
        userController.createUser(user);
        User updateUser = new User(1,"email@yandex.ru", "", "name",
                LocalDate.of(1996,12,05));

        final ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> userController.updateUser(updateUser));

        Assertions.assertEquals("Validation Exception!", e.getMessage());
    }

    @Test
    public void updateUserLoginSpaceBar() {
        User user = new User("email@yandex.ru", "login", "name",
                LocalDate.of(1996,12,05));
        userController.createUser(user);
        User updateUser = new User(1,"email@yandex.ru", "new Login", "name",
                LocalDate.of(1996,12,05));

        final ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> userController.updateUser(updateUser));

        Assertions.assertEquals("Validation Exception!", e.getMessage());
    }

    @Test
    public void updateUserNameEmpty() {
        User user = new User("email@yandex.ru", "login", "name",
                LocalDate.of(1996,12,05));
        userController.createUser(user);
        User updateUser = new User(1,"email@yandex.ru", "login", "",
                LocalDate.of(1996,12,05));
        userController.updateUser(updateUser);

        User exceptedUser = new User(1,"email@yandex.ru", "login", "login",
                LocalDate.of(1996,12,05));

        Assertions.assertEquals(exceptedUser, userController.getUserById(1));
    }

    @Test
    public void updateUserBirthdayFuture() {
        User user = new User("email@yandex.ru", "login", "name",
                LocalDate.of(1996,12,05));
        userController.createUser(user);
        User updateUser = new User(1,"email@yandex.ru", "login", "name",
                LocalDate.of(2025,12,05));

        final ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> userController.updateUser(updateUser));

        Assertions.assertEquals("Validation Exception!", e.getMessage());
    }
}