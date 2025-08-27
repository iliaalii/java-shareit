package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    @Null(groups = ItemRequestDto.Create.class, message = "ID не должен быть задан при создании")
    private Long id;

    @NotBlank(groups = {ItemRequestDto.Create.class}, message = "Описание не может быть пустым")
    @Size(groups = ItemRequestDto.Create.class,
            max = 512, message = "Описание не должно превышать 512 символов")
    private String description;

    @Null(groups = ItemRequestDto.Create.class, message = "Не нужно задавать при создании")
    private LocalDateTime created;

    @Null(groups = ItemRequestDto.Create.class, message = "Не нужно задавать при создании")
    private List<ItemDto> items;

    public interface Create {
    }
}
