package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

public interface UserService {
    User add(User user);

    User find(int id);

    void update(User user);

    void delete(int id);
}
