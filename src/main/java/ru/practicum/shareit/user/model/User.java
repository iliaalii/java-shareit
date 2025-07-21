package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"id"})
public class User {
    Integer id;
    String name;
    String email;
}
