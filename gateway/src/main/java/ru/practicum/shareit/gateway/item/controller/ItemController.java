package ru.practicum.shareit.gateway.item.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.item.client.ItemClient;
import ru.practicum.shareit.gateway.item.dto.CommentDto;
import ru.practicum.shareit.gateway.item.dto.ItemDto;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> add(@RequestHeader(HEADER_USER_ID) @Positive long userId,
                                      @Validated(ItemDto.Create.class) @RequestBody ItemDto itemDto) {
        return itemClient.add(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> find(@RequestHeader(HEADER_USER_ID) @Positive long userId,
                                       @PathVariable @Positive long itemId) {
        return itemClient.find(userId, itemId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findAllByUser(@RequestHeader(HEADER_USER_ID) @Positive long userId) {
        return itemClient.findAllByUser(userId);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> update(@RequestHeader(HEADER_USER_ID) @Positive long userId,
                                         @PathVariable @Positive long itemId,
                                         @Validated(ItemDto.Update.class) @RequestBody ItemDto itemDto) {
        return itemClient.update(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> search(@RequestHeader(HEADER_USER_ID) @Positive long userId, @RequestParam String text) {
        if (text == null || text.isBlank()) {
            ResponseEntity.ok(List.of());
        }
        return itemClient.search(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> addComment(@RequestHeader(HEADER_USER_ID) @Positive long userId,
                                             @PathVariable @Positive long itemId,
                                             @Validated(CommentDto.Create.class) @RequestBody CommentDto comment) {
        return itemClient.addComment(userId, itemId, comment);
    }
}
