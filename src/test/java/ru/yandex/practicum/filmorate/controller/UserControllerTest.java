package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.InMemoryUserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

class UserControllerTest {

    private UserController userController;
    private InMemoryUserService userService;
    private InMemoryUserStorage userStorage;

    @BeforeEach
    private void setUp() {
        userStorage = new InMemoryUserStorage();
        userService = new InMemoryUserService(userStorage);
        userController = new UserController(userService);
    }

    @Test
    public void createUser() {
        User user = new User("email@yandex.ru", "login", "name",
                LocalDate.of(1996,12,05));
        userController.createUser(user);

        User exceptedUser = new User(1,"email@yandex.ru", "login", "name",
                LocalDate.of(1996,12,05));

        Assertions.assertEquals(exceptedUser, userService.getUserById(1));
    }

    @Test
    public void createUserEmailEmpty() {
        User user = new User("", "login", "name",
                LocalDate.of(1996,12,05));

        final ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> userController.createUser(user));

        Assertions.assertEquals("Validation Exception!" +
                "The email cannot be empty and must contain the character @;\n" +
                "The login cannot be empty and contain spaces;\n" +
                "The name to display can be empty — in this case, the login will be used;\n" +
                "The date of birth cannot be in the future.", e.getMessage());
    }

    @Test
    public void createUserLoginEmpty() {
        User user = new User("email@yandex.ru", "", "name",
                LocalDate.of(1996,12,05));

        final ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> userController.createUser(user));

        Assertions.assertEquals("Validation Exception!" +
                "The email cannot be empty and must contain the character @;\n" +
                "The login cannot be empty and contain spaces;\n" +
                "The name to display can be empty — in this case, the login will be used;\n" +
                "The date of birth cannot be in the future.", e.getMessage());
    }

    @Test
    public void createUserLoginSpaceBar() {
        User user = new User("email@yandex.ru", "new Login", "name",
                LocalDate.of(1996,12,05));

        final ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> userController.createUser(user));

        Assertions.assertEquals("Validation Exception!" +
                "The email cannot be empty and must contain the character @;\n" +
                "The login cannot be empty and contain spaces;\n" +
                "The name to display can be empty — in this case, the login will be used;\n" +
                "The date of birth cannot be in the future.", e.getMessage());
    }

    @Test
    public void createUserNameEmpty() {
        User user = new User("email@yandex.ru", "login", "",
                LocalDate.of(1996,12,05));
        userController.createUser(user);

        User exceptedUser = new User(1,"email@yandex.ru", "login", "login",
                LocalDate.of(1996,12,05));

        Assertions.assertEquals(exceptedUser, userService.getUserById(1));
    }

    @Test
    public void createUserBirthdayFuture() {
        User user = new User("email@yandex.ru", "login", "",
                LocalDate.of(2025,12,05));

        final ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> userController.createUser(user));

        Assertions.assertEquals("Validation Exception!" +
                "The email cannot be empty and must contain the character @;\n" +
                "The login cannot be empty and contain spaces;\n" +
                "The name to display can be empty — in this case, the login will be used;\n" +
                "The date of birth cannot be in the future.", e.getMessage());
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

        Assertions.assertEquals(exceptedUser, userService.getUserById(1));
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

        Assertions.assertEquals("Validation Exception!" +
                "The email cannot be empty and must contain the character @;\n" +
                "The login cannot be empty and contain spaces;\n" +
                "The name to display can be empty — in this case, the login will be used;\n" +
                "The date of birth cannot be in the future.", e.getMessage());
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

        Assertions.assertEquals("Validation Exception!" +
                "The email cannot be empty and must contain the character @;\n" +
                "The login cannot be empty and contain spaces;\n" +
                "The name to display can be empty — in this case, the login will be used;\n" +
                "The date of birth cannot be in the future.", e.getMessage());
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

        Assertions.assertEquals("Validation Exception!" +
                "The email cannot be empty and must contain the character @;\n" +
                "The login cannot be empty and contain spaces;\n" +
                "The name to display can be empty — in this case, the login will be used;\n" +
                "The date of birth cannot be in the future.", e.getMessage());
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

        Assertions.assertEquals(exceptedUser, userService.getUserById(1));
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

        Assertions.assertEquals("Validation Exception!" +
                "The email cannot be empty and must contain the character @;\n" +
                "The login cannot be empty and contain spaces;\n" +
                "The name to display can be empty — in this case, the login will be used;\n" +
                "The date of birth cannot be in the future.", e.getMessage());
    }

}