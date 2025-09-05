package ru.practicum.shareit.gateway.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.gateway.request.dto.ItemRequestDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRequestDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenIdNotNull() {
        ItemRequestDto dto = new ItemRequestDto(
                1L,
                "Описание"
        );

        Set<ConstraintViolation<ItemRequestDto>> violations =
                validator.validate(dto, ItemRequestDto.Create.class);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("ID не должен быть задан при создании");
    }

    @Test
    void whenDescriptionBlank() {
        ItemRequestDto dto = new ItemRequestDto(
                null,
                "   "
        );

        Set<ConstraintViolation<ItemRequestDto>> violations =
                validator.validate(dto, ItemRequestDto.Create.class);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Описание не может быть пустым");
    }

    @Test
    void whenDescriptionTooLong() {
        String longDesc = "a".repeat(600);
        ItemRequestDto dto = new ItemRequestDto(
                null,
                longDesc
        );

        Set<ConstraintViolation<ItemRequestDto>> violations =
                validator.validate(dto, ItemRequestDto.Create.class);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Описание не должно превышать 512 символов");
    }
}
