package ru.practicum.shareit.item.service;


import ru.practicum.shareit.item.dto.ItemDto;


import java.util.List;

public interface ItemService {
    ItemDto add(int userId, ItemDto itemDto);

    ItemDto find(int itemId);

    List<ItemDto> findAllByUser(int userId);

    ItemDto update(int userId, int itemId, ItemDto itemDto);

    List<ItemDto> search(int userId, String text);
}
