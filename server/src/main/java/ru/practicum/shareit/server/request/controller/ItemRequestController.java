package ru.practicum.shareit.server.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.server.request.dto.ItemRequestDto;
import ru.practicum.shareit.server.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto add(@RequestHeader(HEADER_USER_ID) Long userId,
                              @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.add(userId, itemRequestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDto> findAllOwnRequests(@RequestHeader(HEADER_USER_ID) Long userId) {
        return itemRequestService.findAllOwnRequests(userId);
    }


    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDto> findAll(@RequestHeader(HEADER_USER_ID) Long userId) {
        return itemRequestService.findAll(userId);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDto find(@PathVariable Long requestId) {
        return itemRequestService.find(requestId);
    }
}
