package ru.practicum.shareit.server.user.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import ru.practicum.shareit.server.exception.NotFoundException;
import ru.practicum.shareit.server.user.dto.UserDto;
import ru.practicum.shareit.server.user.mapper.UserMapper;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.dao.UserRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceDB implements UserService {
    private final UserRepository userStorage;
    private final UserMapper userMapper;

    @Override
    public UserDto add(UserDto userDto) {
        log.info("Обработка запросна на добавление нового пользователя");
        User user = userMapper.toUser(userDto);
        return userMapper.toUserDto(userStorage.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto find(Long id) {
        log.info("Обработка на поиск пользователя");
        User user = userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден"));
        return userMapper.toUserDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        log.info("Обработка запроса на поиск всех пользователей");
        return userStorage.findAll().stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        log.info("Обработка запроса на обновление пользователя {}", id);
        User user = userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден"));
        userMapper.updateUser(user, userDto);
        return userMapper.toUserDto(userStorage.save(user));
    }

    @Override
    public void delete(Long id) {
        log.info("Обработка запроса на удаление пользователя {}", id);
        userStorage.deleteById(id);
    }
}
