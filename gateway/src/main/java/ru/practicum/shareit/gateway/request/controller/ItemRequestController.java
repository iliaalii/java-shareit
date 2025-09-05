package ru.practicum.shareit.gateway.request.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.request.client.ItemRequestClient;
import ru.practicum.shareit.gateway.request.dto.ItemRequestDto;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> add(@RequestHeader(HEADER_USER_ID) @Positive Long userId,
                                      @Validated(ItemRequestDto.Create.class) @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestClient.add(userId, itemRequestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findAllOwnRequests(@RequestHeader(HEADER_USER_ID) @Positive Long userId) {
        return itemRequestClient.findAllOwnRequests(userId);
    }


    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findAll(@RequestHeader(HEADER_USER_ID) @Positive Long userId) {
        return itemRequestClient.findAll(userId);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> find(@PathVariable @Positive Long requestId) {
        return itemRequestClient.find(requestId);
    }
}
