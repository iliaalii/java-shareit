package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository userStorage;

    @Override
    public User add(User user) {
        return userStorage.add(user);
    }

    @Override
    public User find(int id) {
        return userStorage.find(id);
    }

    @Override
    public void update(User user) {
        userStorage.update(user);
    }

    @Override
    public void delete(int id) {
        userStorage.delete(id);
    }
}
