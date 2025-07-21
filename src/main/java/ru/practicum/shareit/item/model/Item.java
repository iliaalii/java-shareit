package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"id"})
public class Item {
    Integer id;
    String name;
    String description;
    boolean available;
    Integer owner;
    Integer request;
}
