package ru.practicum.shareit.item.service;


import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;


import java.util.List;

public interface ItemService {
    ItemDto add(Long userId, ItemDto itemDto);

    ItemDto find(Long userId, Long itemId);

    List<ItemDto> findAllByUser(Long userId);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    List<ItemDto> search(Long userId, String text);

    CommentDto addComment(Long userId, Long itemId, CommentDto comment);
}
