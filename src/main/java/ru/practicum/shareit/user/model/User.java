package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"id"})
public class User {
    Integer id;
    @NotBlank(message = "Логин не может быть пустым")
    String name;
    @NotBlank(message = "Почта должна быть указана")
    @Email
    String email;
}
