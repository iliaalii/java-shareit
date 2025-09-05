package ru.practicum.shareit.gateway.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto {
    @Null(groups = Create.class, message = "ID не должен быть задан при создании")
    private Long id;

    @NotBlank(groups = {Create.class}, message = "Текст комментария обязателен")
    @Size(groups = {Create.class},
            max = 512, message = "Текст комментария не должно превышать 512 символов")
    private String text;

    public interface Create {
    }
}
