package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    @Null(groups = ItemDto.Create.class, message = "ID не должен быть задан при создании")
    private Integer id;

    @NotBlank(groups = {ItemDto.Create.class}, message = "Название не может быть пустым")
    private String name;

    @NotBlank(groups = {ItemDto.Create.class}, message = "Описание не может быть пустым")
    @Size(groups = {ItemDto.Create.class, ItemDto.Update.class}, max = 200, message = "Описание не должно превышать 200 символов")
    private String description;

    @NotNull(groups = {ItemDto.Create.class}, message = "Доступность должна быть указана")
    private Boolean available;

    public interface Create {
    }

    public interface Update {
    }
}
