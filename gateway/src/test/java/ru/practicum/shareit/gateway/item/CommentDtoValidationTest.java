package ru.practicum.shareit.gateway.item;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.gateway.item.dto.CommentDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CommentDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenIdNotNull() {
        CommentDto dto = new CommentDto(1L, "Комментарий");
        Set<ConstraintViolation<CommentDto>> violations =
                validator.validate(dto, CommentDto.Create.class);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("ID не должен быть задан при создании");
    }

    @Test
    void whenTextBlank() {
        CommentDto dto = new CommentDto(null, "   ");
        Set<ConstraintViolation<CommentDto>> violations =
                validator.validate(dto, CommentDto.Create.class);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Текст комментария обязателен");
    }

    @Test
    void whenTextTooLong() {
        String longText = "a".repeat(600);
        CommentDto dto = new CommentDto(null, longText);
        Set<ConstraintViolation<CommentDto>> violations =
                validator.validate(dto, CommentDto.Create.class);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Текст комментария не должно превышать 512 символов");
    }
}
