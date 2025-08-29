package ru.practicum.shareit.server.request.service;

import ru.practicum.shareit.server.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto add(Long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> findAllOwnRequests(Long userId);

    List<ItemRequestDto> findAll(Long userId);

    ItemRequestDto find(Long requestId);
}
