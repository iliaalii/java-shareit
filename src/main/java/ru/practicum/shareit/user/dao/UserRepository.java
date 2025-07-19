package ru.practicum.shareit.user.dao;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
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
        nextId++;
        return user;
    }

    public User find(int id) {
        return users.get(id);
    }

    public void update(User user) {
        users.put(user.getId(), user);
    }

    public void delete(int id) {
        users.remove(id);
    }
}
