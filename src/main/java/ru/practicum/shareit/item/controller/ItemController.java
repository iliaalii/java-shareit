package ru.practicum.shareit.item.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemService itemService;
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto add(@RequestHeader(HEADER_USER_ID) @Positive Long userId,
                       @Validated(ItemDto.Create.class) @RequestBody ItemDto itemDto) {
        return itemService.add(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto find(@RequestHeader(HEADER_USER_ID) @Positive Long userId,
                        @PathVariable @Positive Long itemId) {
        return itemService.find(userId, itemId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> findAllByUser(@RequestHeader(HEADER_USER_ID) @Positive Long userId) {
        return itemService.findAllByUser(userId);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto update(@RequestHeader(HEADER_USER_ID) @Positive Long userId,
                          @PathVariable @Positive Long itemId,
                          @Validated(ItemDto.Update.class) @RequestBody ItemDto itemDto) {
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> search(@RequestHeader(HEADER_USER_ID) @Positive Long userId, @RequestParam String text) {
        return itemService.search(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto addComment(@RequestHeader(HEADER_USER_ID) @Positive Long userId,
                                 @PathVariable @Positive Long itemId,
                                 @Validated(CommentDto.Create.class) @RequestBody CommentDto comment) {
        return itemService.addComment(userId,itemId, comment);
    }
}
