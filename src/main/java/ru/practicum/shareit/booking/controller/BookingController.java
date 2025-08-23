package ru.practicum.shareit.booking.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;


import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingService bookingService;
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto add(@RequestHeader(HEADER_USER_ID) @Positive Long userId,
                          @Validated(BookingDto.Create.class) @RequestBody BookingDto bookingDto) {
        return bookingService.add(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto update(@RequestHeader(HEADER_USER_ID) @Positive Long userId,
                             @PathVariable @Positive Long bookingId,
                             @RequestParam(name = "approved") boolean approved) {
        return bookingService.update(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto find(@RequestHeader(HEADER_USER_ID) @Positive Long userId,
                           @PathVariable @Positive Long bookingId) {
        return bookingService.find(userId, bookingId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> findAll(@RequestHeader(HEADER_USER_ID) @Positive Long userId,
                                    @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingService.findAll(userId, state);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> findAllByOwner(@RequestHeader(HEADER_USER_ID) @Positive Long userId,
                                           @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingService.findAllByOwner(userId, state);
    }
}
