package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDto {
    @Null(groups = {BookingDto.Create.class}, message = "ID не должен быть задан при создании")
    private Long id;

    @NotNull(groups = {BookingDto.Create.class}, message = "Время начала бронирования не может быть пустым")
    private LocalDateTime start;

    @NotNull(groups = {BookingDto.Create.class}, message = "Время конца бронирования не может быть пустым")
    private LocalDateTime end;

    @NotNull(groups = {BookingDto.Create.class}, message = "Id вещи не может быть пустым")
    @Positive(message = "Id вещи должен быть положительным числом")
    private Long itemId;

    @Null(groups = {BookingDto.Create.class}, message = "Предмет задается автоматически")
    private ItemDto item;

    @Null(groups = {BookingDto.Create.class}, message = "Пользователь задается автоматически")
    private UserDto booker;

    @Null(groups = {BookingDto.Create.class}, message = "Статус задается автоматически")
    private String status;

    public interface Create {
    }
}
