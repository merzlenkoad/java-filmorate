package ru.yandex.practicum.filmorate.storage.friend;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureCache
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FriendDbStorageTest {

    private final FriendDbStorage friendDbStorage;
    private final UserDbStorage userDbStorage;
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
    void addFriendAndGetFriend() {
        firstUser = userDbStorage.addUser(firstUser);
        secondUser = userDbStorage.addUser(secondUser);
        friendDbStorage.addFriend(firstUser.getId(), secondUser.getId());

        assertThat(friendDbStorage.getFriends(firstUser.getId())).hasSize(1);
        assertThat(friendDbStorage.getFriends(firstUser.getId())).contains(secondUser.getId());
        assertThat(friendDbStorage.getFriends(secondUser.getId())).hasSize(0);
    }

    @Test
    void deleteFriend() {
        firstUser = userDbStorage.addUser(firstUser);
        secondUser = userDbStorage.addUser(secondUser);
        friendDbStorage.addFriend(firstUser.getId(), secondUser.getId());
        friendDbStorage.deleteFriend(firstUser.getId(), secondUser.getId());

        assertThat(friendDbStorage.getFriends(firstUser.getId())).hasSize(0);
    }
}