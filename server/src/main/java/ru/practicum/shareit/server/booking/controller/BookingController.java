package ru.practicum.shareit.server.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.server.booking.dto.BookingDto;
import ru.practicum.shareit.server.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto add(@RequestHeader(HEADER_USER_ID) Long userId,
                          @RequestBody BookingDto bookingDto) {
        return bookingService.add(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto update(@RequestHeader(HEADER_USER_ID) Long userId,
                             @PathVariable Long bookingId,
                             @RequestParam(name = "approved") boolean approved) {
        return bookingService.update(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto find(@RequestHeader(HEADER_USER_ID) Long userId,
                           @PathVariable Long bookingId) {
        return bookingService.find(userId, bookingId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> findAll(@RequestHeader(HEADER_USER_ID) Long userId,
                                    @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingService.findAll(userId, state);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> findAllByOwner(@RequestHeader(HEADER_USER_ID) Long userId,
                                           @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingService.findAllByOwner(userId, state);
    }
}
