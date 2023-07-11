package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureCache
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {

    private final UserStorage userStorage;
    private User firstUser;
    private User secondUser;

    @BeforeEach
    public void setUp() {
        firstUser = User.builder()
                .name("first user")
                .login("first login")
                .email("firstuser@email.ru")
                .birthday(LocalDate.of(1996, 12, 5))
                .build();

        secondUser = User.builder()
                .name("second user")
                .login("second login")
                .email("seconduser@email.ru")
                .birthday(LocalDate.of(1997, 11, 10))
                .build();
    }

    @Test
    void addUserAndGetUserById() {
        firstUser = userStorage.addUser(firstUser);

        Optional<User> testFirstUser = Optional.ofNullable(userStorage.getUserById(firstUser.getId()));
        assertThat(testFirstUser)
                .hasValueSatisfying(user -> assertThat(user)
                        .hasFieldOrPropertyWithValue("id", firstUser.getId())
                        .hasFieldOrPropertyWithValue("name", "first user"));
    }

    @Test
    void updateUser() {
        firstUser = userStorage.addUser(firstUser);
        User updateUser = firstUser;
        updateUser.setName("test user");

        Optional<User> testUpdateUser = Optional.ofNullable(userStorage.updateUser(updateUser));
        assertThat(testUpdateUser)
                .hasValueSatisfying(user -> assertThat(user)
                        .hasFieldOrPropertyWithValue("name", "test user"));
    }

    @Test
    void getUsers() {
        firstUser = userStorage.addUser(firstUser);
        secondUser = userStorage.addUser(secondUser);

        List<User> expectedUsers = userStorage.getUsers();

        assertThat(expectedUsers).contains(firstUser).contains(secondUser);
    }

    @Test
    void deleteUserById() {
        firstUser = userStorage.addUser(firstUser);
        userStorage.deleteUserById(firstUser.getId());

        List<User> expectedUsers = userStorage.getUsers();

        assertThat(expectedUsers).hasSize(0);
    }
}