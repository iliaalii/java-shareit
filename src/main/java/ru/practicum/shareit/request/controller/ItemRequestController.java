package ru.practicum.shareit.request.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto add(@RequestHeader(HEADER_USER_ID) @Positive Long userId,
                              @Validated(ItemRequestDto.Create.class) @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.add(userId, itemRequestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDto> findAllOwnRequests(@RequestHeader(HEADER_USER_ID) @Positive Long userId) {
        return itemRequestService.findAllOwnRequests(userId);
    }


    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDto> findAll(@RequestHeader(HEADER_USER_ID) @Positive Long userId) {
        return itemRequestService.findAll(userId);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDto find(@PathVariable @Positive Long requestId) {
        return itemRequestService.find(requestId);
    }
}
