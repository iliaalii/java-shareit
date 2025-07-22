package ru.practicum.shareit.item.dao;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ItemRepository {
    private final Map<Integer, Item> items = new HashMap<>();
    private int nextId = 1;

    public Item add(Item item) {
        item.setId(nextId);
        items.put(nextId, item);
        log.info("Добавлена новая вещь, под номером {}", nextId);
        nextId++;
        return items.get(item.getId());
    }

    public Item find(int id) {
        Item item = items.get(id);
        if (item == null) {
            log.info("Вещь под номером {}, не найдена", id);
            throw new NotFoundException("Вещь под номером " + id + " не найдена");
        }
        log.info("Нашли вещь под номером {}", id);
        return item;
    }

    public List<Item> findAllByUser(int userId) {
        log.info("Ищем все вещи пользователя {}", userId);
        return items.values().stream()
                .filter(item -> item.getOwner() != null && item.getOwner() == userId)
                .toList();
    }

    public Item update(Item item) {
        items.put(item.getId(), item);
        log.info("Обновили вещь под номером {}", item.getId());
        return items.get(item.getId());
    }

    public List<Item> search(String text) {
        log.info("Выводим поиск вещей доступных к аренде, по ключевым словам");
        String searchText = text.toLowerCase();
        return items.values().stream()
                .filter(item -> item.isAvailable())
                .filter(item -> (item.getName() != null && item.getName().toLowerCase().contains(searchText)) ||
                        (item.getDescription() != null && item.getDescription().toLowerCase().contains(searchText)))
                .toList();
    }
}
