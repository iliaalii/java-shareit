package ru.practicum.shareit.gateway.item;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.gateway.item.dto.ItemDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ItemDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenIdNotNull() {
        ItemDto dto = new ItemDto(
                100L,
                "Молоток",
                "Описание",
                true,
                1L
        );

        Set<ConstraintViolation<ItemDto>> violations =
                validator.validate(dto, ItemDto.Create.class);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("ID не должен быть задан при создании");
    }

    @Test
    void whenNameBlank() {
        ItemDto dto = new ItemDto(
                null,
                "   ",
                "Описание",
                true,
                1L
        );

        Set<ConstraintViolation<ItemDto>> violations =
                validator.validate(dto, ItemDto.Create.class);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Название не может быть пустым");
    }

    @Test
    void whenDescriptionTooLong() {
        String longDesc = "a".repeat(250);
        ItemDto dto = new ItemDto(
                null,
                "Молоток",
                longDesc,
                true,
                1L
        );

        Set<ConstraintViolation<ItemDto>> violations =
                validator.validate(dto, ItemDto.Create.class);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Описание не должно превышать 200 символов");
    }

    @Test
    void whenAvailableNull() {
        ItemDto dto = new ItemDto(
                null,
                "Молоток",
                "Описание",
                null,
                1L
        );

        Set<ConstraintViolation<ItemDto>> violations =
                validator.validate(dto, ItemDto.Create.class);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Доступность должна быть указана");
    }

    @Test
    void whenRequestIdNegative() {
        ItemDto dto = new ItemDto(
                null,
                "Молоток",
                "Описание",
                true,
                -5L
        );

        Set<ConstraintViolation<ItemDto>> violations =
                validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("должно быть положительным");
    }
}
