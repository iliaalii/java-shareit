package ru.practicum.shareit.server.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.server.user.dao.UserRepository;
import ru.practicum.shareit.server.user.dto.UserDto;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.service.UserService;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setName("name");
        user.setEmail("test@mail.com");
    }

    @Test
    void testFind() {
        user = userRepository.save(user);
        UserDto foundUser = userService.find(user.getId());
        assertThat(foundUser.getId()).isEqualTo(user.getId());
    }

    @Test
    void testUpdate() {
        user = userRepository.save(user);

        UserDto updateUser = new UserDto();
        user.setEmail("other@mail.com");
        updateUser = userService.update(user.getId(), updateUser);

        assertThat(updateUser.getId()).isEqualTo(user.getId());
        assertThat(updateUser.getName()).isEqualTo("name");
        assertThat(updateUser.getEmail()).isEqualTo("other@mail.com");
    }
}
