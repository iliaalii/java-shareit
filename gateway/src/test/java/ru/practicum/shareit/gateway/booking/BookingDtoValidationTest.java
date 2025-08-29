package ru.practicum.shareit.gateway.booking;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.gateway.booking.dto.BookingDto;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BookingDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenIdNotNullOnCreate() {
        BookingDto dto = new BookingDto(
                100L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                1L
        );

        Set<ConstraintViolation<BookingDto>> violations =
                validator.validate(dto, BookingDto.Create.class);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("ID не должен быть задан при создании");
    }

    @Test
    void whenStartNull() {
        BookingDto dto = new BookingDto(
                null,
                null,
                LocalDateTime.now().plusDays(2),
                1L
        );

        Set<ConstraintViolation<BookingDto>> violations =
                validator.validate(dto, BookingDto.Create.class);

        assertThat(violations).anyMatch(v ->
                v.getMessage().equals("Время начала бронирования не может быть пустым"));
    }

    @Test
    void whenEndNull() {
        BookingDto dto = new BookingDto(
                null,
                LocalDateTime.now().plusDays(1),
                null,
                1L
        );

        Set<ConstraintViolation<BookingDto>> violations =
                validator.validate(dto, BookingDto.Create.class);

        assertThat(violations).anyMatch(v ->
                v.getMessage().equals("Время конца бронирования не может быть пустым"));
    }

    @Test
    void whenItemIdNull() {
        BookingDto dto = new BookingDto(
                null,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                null
        );

        Set<ConstraintViolation<BookingDto>> violations =
                validator.validate(dto, BookingDto.Create.class);

        assertThat(violations).anyMatch(v ->
                v.getMessage().equals("Id вещи не может быть пустым"));
    }

    @Test
    void whenItemIdNotPositive() {
        BookingDto dto = new BookingDto(
                null,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                0L
        );

        Set<ConstraintViolation<BookingDto>> violations =
                validator.validate(dto);

        assertThat(violations).anyMatch(v ->
                v.getMessage().equals("Id вещи должен быть положительным числом"));
    }
}
