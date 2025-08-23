package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto add(BookingDto bookingDto, Long userId);

    BookingDto update(Long userId, Long bookingId, boolean approved);

    BookingDto find(Long userId, Long bookingId);

    List<BookingDto> findAll(Long userId, String state);

    List<BookingDto> findAllByOwner(Long userId, String state);
}
