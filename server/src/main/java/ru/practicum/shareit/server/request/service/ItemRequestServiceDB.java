package ru.practicum.shareit.server.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.server.exception.NotFoundException;
import ru.practicum.shareit.server.item.dao.ItemRepository;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.item.mapper.ItemMapper;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.request.dao.ItemRequestRepository;
import ru.practicum.shareit.server.request.dto.ItemRequestDto;
import ru.practicum.shareit.server.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.server.request.model.ItemRequest;
import ru.practicum.shareit.server.user.dao.UserRepository;
import ru.practicum.shareit.server.user.model.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceDB implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemMapper itemMapper;

    @Override
    public ItemRequestDto add(Long userId, ItemRequestDto itemRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        ItemRequest request = itemRequestMapper.toEntity(itemRequestDto, user);
        return itemRequestMapper.toDto(itemRequestRepository.save(request));
    }

    @Override
    public List<ItemRequestDto> findAllOwnRequests(Long userId) {
        List<ItemRequest> requests = itemRequestRepository.findByRequesterIdOrderByCreatedDesc(userId);

        if (requests.isEmpty()) {
            return List.of();
        }

        List<Long> requestIds = requests.stream()
                .map(ItemRequest::getId)
                .toList();
        List<Item> items = itemRepository.findByRequestIdIn(requestIds);

        Map<Long, List<ItemDto>> itemsByRequest = items.stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.groupingBy(ItemDto::getRequestId));

        return requests.stream()
                .map(r -> itemRequestMapper
                        .toDtoWithAnswers(r, itemsByRequest.getOrDefault(r.getId(), List.of()))).toList();
    }

    @Override
    public List<ItemRequestDto> findAll(Long userId) {
        List<ItemRequest> requests = itemRequestRepository.findByRequesterIdNotOrderByCreatedDesc(userId);

        if (requests.isEmpty()) {
            return List.of();
        }

        return requests.stream()
                .map(itemRequestMapper::toDto)
                .toList();
    }

    @Override
    public ItemRequestDto find(Long requestId) {
        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден"));
        List<ItemDto> items = itemRepository.findByRequest_Id(requestId).stream()
                .map(itemMapper::toItemDtoForRequest).toList();
        return itemRequestMapper.toDtoWithAnswers(request, items);
    }
}
