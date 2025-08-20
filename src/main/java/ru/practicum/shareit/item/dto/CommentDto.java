package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto {
    @Null(groups = CommentDto.Create.class, message = "ID не должен быть задан при создании")
    private Long id;

    @NotBlank(groups = {CommentDto.Create.class}, message = "Текст комментария обязателен")
    @Size(groups = {CommentDto.Create.class},
            max = 512, message = "Текст комментария не должно превышать 512 символов")
    private String text;

    @Null(groups = CommentDto.Create.class, message = "Не нужно задавать при создании")
    private String authorName;

    @Null(groups = CommentDto.Create.class, message = "Не нужно задавать при создании")
    private LocalDateTime created;

    public interface Create {
    }
}
