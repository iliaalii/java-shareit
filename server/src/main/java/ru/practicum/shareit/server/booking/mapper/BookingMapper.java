package ru.practicum.shareit.server.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ru.practicum.shareit.server.booking.dto.BookingDto;
import ru.practicum.shareit.server.booking.model.Booking;
import ru.practicum.shareit.server.item.mapper.ItemMapper;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.user.mapper.UserMapper;
import ru.practicum.shareit.server.user.model.User;

@Mapper(componentModel = "spring", uses = {ItemMapper.class, UserMapper.class})
public interface BookingMapper {
    @Mapping(target = "itemId", ignore = true)
    @Mapping(target = "item", source = "item", qualifiedByName = "toItemDto")
    @Mapping(target = "booker", source = "booker")
    @Mapping(target = "status", source = "status")
    BookingDto toDTO(Booking booking);

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "item", source = "item")
    @Mapping(target = "booker", source = "booker")
    @Mapping(target = "status", ignore = true)
    Booking toBooking(BookingDto dto, Item item, User booker);
}
