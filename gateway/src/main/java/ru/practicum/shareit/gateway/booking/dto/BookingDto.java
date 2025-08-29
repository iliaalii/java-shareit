package ru.practicum.shareit.gateway.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDto {
    @Null(groups = {Create.class}, message = "ID не должен быть задан при создании")
    private Long id;

    @NotNull(groups = {Create.class}, message = "Время начала бронирования не может быть пустым")
    private LocalDateTime start;

    @NotNull(groups = {Create.class}, message = "Время конца бронирования не может быть пустым")
    private LocalDateTime end;

    @NotNull(groups = {Create.class}, message = "Id вещи не может быть пустым")
    @Positive(message = "Id вещи должен быть положительным числом")
    private Long itemId;

    public interface Create {
    }
}
