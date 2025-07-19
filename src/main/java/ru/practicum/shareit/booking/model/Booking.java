package ru.practicum.shareit.booking.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = {"id"})
public class Booking {
    Integer id;
    LocalDateTime start;
    LocalDateTime end;
    Item item;
    ru.practicum.shareit.user.model.User booker;
    Status status;
}

