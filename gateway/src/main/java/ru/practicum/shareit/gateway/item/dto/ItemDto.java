package ru.practicum.shareit.gateway.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDto {
    @Null(groups = Create.class, message = "ID не должен быть задан при создании")
    private Long id;

    @NotBlank(groups = {Create.class}, message = "Название не может быть пустым")
    private String name;

    @NotBlank(groups = {Create.class}, message = "Описание не может быть пустым")
    @Size(groups = {Create.class, Update.class},
            max = 200, message = "Описание не должно превышать 200 символов")
    private String description;

    @NotNull(groups = {Create.class}, message = "Доступность должна быть указана")
    private Boolean available;

    @Positive(message = "должно быть положительным")
    private Long requestId;

    public interface Create {
    }

    public interface Update {
    }
}
