package ru.practicum.shareit.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = {"id"})
public class ItemRequest {
    int id;
    String description;
    User requestor; // или user
    LocalDateTime created;
}
