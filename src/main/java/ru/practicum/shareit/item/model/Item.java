package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.request.ItemRequest;

@Data
@EqualsAndHashCode(of = {"id"})
public class Item {
    Integer id;
    String name;
    String description;
    boolean available;
    ru.practicum.shareit.user.model.User owner;
    ItemRequest request;
}
