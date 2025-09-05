package ru.practicum.shareit.server.user.service;

import ru.practicum.shareit.server.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto add(UserDto userDto);

    UserDto find(Long id);

    List<UserDto> findAll();

    UserDto update(Long id, UserDto userDto);

    void delete(Long id);
}
