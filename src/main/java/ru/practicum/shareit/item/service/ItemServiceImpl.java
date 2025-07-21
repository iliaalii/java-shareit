package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemStorage;
    private final UserRepository userStorage;

    @Override
    public ItemDto add(int userId, ItemDto itemDto) {
        log.info("Обработка запроса на добавление вещи от пользователя {}", userId);
        userStorage.userAvailabilityCheck(userId);

        Item item = ItemMapper.toItem(itemDto, userId);
        return ItemMapper.toItemDto(itemStorage.add(item));
    }

    @Override
    public ItemDto find(int itemId) {
        log.info("Обработка запроса на поиск вещи");
        return ItemMapper.toItemDto(itemStorage.find(itemId));
    }

    @Override
    public List<ItemDto> findAllByUser(int userId) {
        log.info("Обработка запроса на поиск вещей");
        userStorage.userAvailabilityCheck(userId);

        return itemStorage.findAllByUser(userId).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public ItemDto update(int userId, int itemId, ItemDto itemDto) {
        log.info("Обработка запроса на обновление вещи, от пользователя {}", userId);
        userStorage.userAvailabilityCheck(userId);

        Item item = itemStorage.find(itemId);
        ItemMapper.updateUser(item, itemDto);
        return ItemMapper.toItemDto(itemStorage.update(item));
    }

    @Override
    public List<ItemDto> search(int userId, String text) {
        log.info("Обработка запроса на поиск пользователем {}", userId);
        userStorage.userAvailabilityCheck(userId);

        if (text == null || text.isBlank()) {
            return List.of();
        }
        return itemStorage.search(text).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }
}
