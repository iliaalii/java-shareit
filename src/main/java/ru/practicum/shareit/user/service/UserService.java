package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto add(UserDto userDto);

    UserDto find(int id);

    List<UserDto> findAll();

    UserDto update(int id, UserDto userDto);

    void delete(int id);
}
