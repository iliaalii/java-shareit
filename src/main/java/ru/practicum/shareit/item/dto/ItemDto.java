package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    @Null(groups = ItemDto.Create.class, message = "ID не должен быть задан при создании")
    private Long id;

    @NotBlank(groups = {ItemDto.Create.class}, message = "Название не может быть пустым")
    private String name;

    @NotBlank(groups = {ItemDto.Create.class}, message = "Описание не может быть пустым")
    @Size(groups = {ItemDto.Create.class, ItemDto.Update.class},
            max = 200, message = "Описание не должно превышать 200 символов")
    private String description;

    @NotNull(groups = {ItemDto.Create.class}, message = "Доступность должна быть указана")
    private Boolean available;

    @Null(groups = ItemDto.Create.class, message = "Не нужно задавать при создании")
    private BookingDto lastBooking;

    @Null(groups = ItemDto.Create.class, message = "Не нужно задавать при создании")
    private BookingDto nextBooking;

    @Null(groups = ItemDto.Create.class, message = "Не нужно задавать при создании")
    private List<CommentDto> comments;

    @Positive
    private Long requestId;

    @Positive
    @Null(groups = ItemDto.Create.class, message = "Не нужно задавать при создании")
    private Long ownerId;

    public interface Create {
    }

    public interface Update {
    }
}
