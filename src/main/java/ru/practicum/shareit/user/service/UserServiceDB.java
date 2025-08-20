package ru.practicum.shareit.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceDB implements UserService {
    private final UserRepository userStorage;

    @Override
    public UserDto add(UserDto userDto) {
        log.info("Обработка запросна на добавление нового пользователя");
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userStorage.save(user));
    }

    @Override
    public UserDto find(Long id) {
        log.info("Обработка на поиск пользователя");
        User user = userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден"));
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> findAll() {
        log.info("Обработка запроса на поиск всех пользователей");
        return userStorage.findAll().stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        log.info("Обработка запроса на обновление пользователя {}", id);
        User user = userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден"));
        UserMapper.updateUser(user, userDto);
        return UserMapper.toUserDto(userStorage.save(user));
    }

    @Override
    public void delete(Long id) {
        log.info("Обработка запроса на удаление пользователя {}", id);
        userStorage.deleteById(id);
    }
}
