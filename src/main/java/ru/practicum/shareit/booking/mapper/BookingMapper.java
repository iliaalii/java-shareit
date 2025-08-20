package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        if (booking == null) return null;

        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setItem(booking.getItem() != null ? ItemMapper.toItemDto(booking.getItem()) : null);
        dto.setBooker(booking.getBooker() != null ? UserMapper.toUserDto(booking.getBooker()) : null);
        dto.setStatus(booking.getStatus().toString());
        return dto;
    }

    public static Booking toBooking(BookingDto dto, Item item, User booker) {
        if (dto == null) return null;

        Booking booking = new Booking();
        booking.setId(dto.getId());
        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        booking.setItem(item);
        booking.setBooker(booker);
        return booking;
    }
}
