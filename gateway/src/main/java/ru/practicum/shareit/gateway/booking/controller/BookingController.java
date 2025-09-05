package ru.practicum.shareit.gateway.booking.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.booking.client.BookingClient;
import ru.practicum.shareit.gateway.booking.dto.BookingDto;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingClient bookingClient;
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> add(@RequestHeader(HEADER_USER_ID) @Positive long userId,
                                      @Validated(BookingDto.Create.class) @RequestBody BookingDto bookingDto) {
        return bookingClient.add(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> update(@RequestHeader(HEADER_USER_ID) @Positive long userId,
                                         @PathVariable @Positive long bookingId,
                                         @RequestParam(name = "approved") boolean approved) {
        return bookingClient.update(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> find(@RequestHeader(HEADER_USER_ID) @Positive long userId,
                                       @PathVariable @Positive long bookingId) {
        return bookingClient.find(userId, bookingId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findAll(@RequestHeader(HEADER_USER_ID) @Positive long userId,
                                          @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingClient.findAllRequests(userId, state);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findAllByOwner(@RequestHeader(HEADER_USER_ID) @Positive long userId,
                                                 @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingClient.findAllByOwner(userId, state);
    }
}
