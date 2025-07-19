package ru.practicum.shareit.item.dao;


import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;

public class ItemRepository {
    private final Map<Integer, User> items = new HashMap<>();
    private int nextId = 1;

    public void add(User item) {
        item.setId(nextId);
        items.put(nextId, item);
        nextId++;
    }

    public User find(int id) {
        return items.get(id);
    }

    public void update(User item) {
        items.put(item.getId(), item);
    }

    public void delete(int id) {
        items.remove(id);
    }
}
