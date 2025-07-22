package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userStorage;

    @Override
    public UserDto add(UserDto userDto) {
        log.info("Обработка запросна на добавление нового пользователя");
        User user = UserMapper.toUser(userDto);
        userStorage.checkDuplicateEmail(user.getEmail());
        return UserMapper.toUserDto(userStorage.add(user));
    }

    @Override
    public UserDto find(int id) {
        log.info("Обработка на поиск пользователя");
        return UserMapper.toUserDto(userStorage.find(id));
    }

    @Override
    public List<UserDto> findAll() {
        log.info("Обработка запроса на поиск всех пользователей");
        return userStorage.findAll().stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto update(int id, UserDto userDto) {
        log.info("Обработка запроса на обновление пользователя {}", id);
        userStorage.checkDuplicateEmail(userDto.getEmail());
        User user = userStorage.find(id);
        UserMapper.updateUser(user, userDto);
        return UserMapper.toUserDto(userStorage.update(user));
    }

    @Override
    public void delete(int id) {
        log.info("Обработка запроса на удаление пользователя {}", id);
        userStorage.delete(id);
    }
}
