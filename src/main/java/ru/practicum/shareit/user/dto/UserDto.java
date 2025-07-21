package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @Null(groups = Create.class, message = "ID не должен быть задан при создании")
    private Integer id;

    @NotBlank(groups = {Create.class}, message = "Имя не может быть пустым")
    private String name;

    @NotBlank(groups = {Create.class}, message = "Email не может быть пустым")
    @Email(groups = {Create.class, Update.class}, message = "Email должен быть валидным")
    private String email;

    public interface Create {
    }

    public interface Update {
    }
}
