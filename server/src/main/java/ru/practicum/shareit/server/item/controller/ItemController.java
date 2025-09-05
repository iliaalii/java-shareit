package ru.practicum.shareit.server.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.server.item.dto.CommentDto;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto add(@RequestHeader(HEADER_USER_ID) Long userId,
                       @RequestBody ItemDto itemDto) {
        return itemService.add(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto find(@RequestHeader(HEADER_USER_ID) Long userId,
                        @PathVariable Long itemId) {
        return itemService.find(userId, itemId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> findAllByUser(@RequestHeader(HEADER_USER_ID) Long userId) {
        return itemService.findAllByUser(userId);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto update(@RequestHeader(HEADER_USER_ID) Long userId,
                          @PathVariable Long itemId,
                          @RequestBody ItemDto itemDto) {
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> search(@RequestHeader(HEADER_USER_ID) Long userId,
                                @RequestParam String text) {
        return itemService.search(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto addComment(@RequestHeader(HEADER_USER_ID) Long userId,
                                 @PathVariable Long itemId,
                                 @RequestBody CommentDto comment) {
        return itemService.addComment(userId, itemId, comment);
    }
}
