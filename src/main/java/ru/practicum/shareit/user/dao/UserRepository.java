package ru.practicum.shareit.user.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepository {
    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    public User add(User user) {
        user.setId(nextId);
        users.put(nextId, user);
        log.info("добавили пользователя {}", nextId);
        nextId++;
        return user;
    }

    public User find(int id) {
        User user = users.get(id);
        if (user == null) {
            log.info("Пользователь под номером {}, не найден", id);
            throw new NotFoundException("Пользователь под номером " + id + " не найдена");
        }
        log.info("Нашли пользователя под номером {}", id);
        return user;
    }

    public List<User> findAll() {
        log.info("Вывод всех пользователей");
        return List.copyOf(users.values());
    }

    public User update(User user) {
        users.put(user.getId(), user);
        log.info("обновили пользователя {}", user.getId());
        return users.get(user.getId());
    }

    public void delete(int id) {
        users.remove(id);
        log.info("пользователь {}, был удален", id);
    }

    public void checkDuplicateEmail(String email) {
        log.info("Проверка повтора почты среди пользователей");
        if (email != null && users.values().stream()
                .map(User::getEmail)
                .anyMatch(e -> e.equals(email))) {
            throw new ConflictException("Пользователь с такой почтой уже существует");
        }
    }

    public void userAvailabilityCheck(int id) {
        log.info("Проверка наличия пользователя {} в хранилище", id);
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь под номером " + id + " не найден");
        }
    }
}
