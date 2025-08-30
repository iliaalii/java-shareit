package ru.practicum.shareit.server.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ru.practicum.shareit.server.exception.NotFoundException;
import ru.practicum.shareit.server.user.dao.UserRepository;
import ru.practicum.shareit.server.user.dto.UserDto;
import ru.practicum.shareit.server.user.mapper.UserMapper;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.service.UserServiceDB;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceDBTest {

    @InjectMocks
    private UserServiceDB userService;

    @Mock
    private UserRepository userStorage;

    @Mock
    private UserMapper userMapper;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("john@mail.com");

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("John");
        userDto.setEmail("john@mail.com");
    }

    @Test
    void add_whenValidUser_thenReturnSavedUserDto() {
        when(userMapper.toUser(userDto)).thenReturn(user);
        when(userStorage.save(user)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.add(userDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(userStorage).save(user);
    }

    @Test
    void find_whenUserExists_thenReturnUserDto() {
        when(userStorage.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.find(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(userStorage).findById(1L);
    }

    @Test
    void find_whenUserNotFound_thenThrow() {
        when(userStorage.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.find(1L));
    }

    @Test
    void findAll_whenUsersExist_thenReturnList() {
        when(userStorage.findAll()).thenReturn(List.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        List<UserDto> result = userService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
    }

    @Test
    void findAll_whenNoUsers_thenReturnEmptyList() {
        when(userStorage.findAll()).thenReturn(List.of());

        List<UserDto> result = userService.findAll();

        assertThat(result).isEmpty();
    }

    @Test
    void update_whenUserExists_thenUpdateAndReturnDto() {
        UserDto updatedDto = new UserDto();
        updatedDto.setName("Jane");
        updatedDto.setEmail("jane@mail.com");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("Jane");
        updatedUser.setEmail("jane@mail.com");

        UserDto resultDto = new UserDto();
        resultDto.setId(1L);
        resultDto.setName("Jane");
        resultDto.setEmail("jane@mail.com");

        when(userStorage.findById(1L)).thenReturn(Optional.of(user));
        doAnswer(invocation -> {
            User u = invocation.getArgument(0);
            UserDto dto = invocation.getArgument(1);
            u.setName(dto.getName());
            u.setEmail(dto.getEmail());
            return null;
        }).when(userMapper).updateUser(user, updatedDto);
        when(userStorage.save(user)).thenReturn(updatedUser);
        when(userMapper.toUserDto(updatedUser)).thenReturn(resultDto);

        UserDto result = userService.update(1L, updatedDto);

        assertThat(result.getName()).isEqualTo("Jane");
        assertThat(result.getEmail()).isEqualTo("jane@mail.com");
        verify(userStorage).findById(1L);
        verify(userStorage).save(user);
    }

    @Test
    void update_whenUserNotFound_thenThrow() {
        when(userStorage.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.update(1L, userDto));
    }

    @Test
    void delete_shouldCallRepositoryDelete() {
        doNothing().when(userStorage).deleteById(1L);

        userService.delete(1L);

        verify(userStorage).deleteById(1L);
    }
}
