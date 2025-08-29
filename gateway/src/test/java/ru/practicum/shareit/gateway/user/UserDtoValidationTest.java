package ru.practicum.shareit.gateway.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.gateway.user.dto.UserDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenIdNotNullOnCreate() {
        UserDto dto = new UserDto(
                10L,
                "name",
                "test@mail.com"
        );

        Set<ConstraintViolation<UserDto>> violations =
                validator.validate(dto, UserDto.Create.class);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("ID не должен быть задан при создании");
    }

    @Test
    void whenNameBlank() {
        UserDto dto = new UserDto(
                null,
                "   ",
                "test@mail.com"
        );

        Set<ConstraintViolation<UserDto>> violations =
                validator.validate(dto, UserDto.Create.class);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Имя не может быть пустым");
    }

    @Test
    void whenEmailBlank() {
        UserDto dto = new UserDto(
                null,
                "name",
                "   "
        );

        Set<ConstraintViolation<UserDto>> violations =
                validator.validate(dto, UserDto.Create.class);

        assertThat(violations).hasSize(2);
        assertThat(violations)
                .anyMatch(v -> v.getMessage().equals("Email не может быть пустым"));
        assertThat(violations)
                .anyMatch(v -> v.getMessage().equals("Email должен быть валидным"));
    }

    @Test
    void whenEmailInvalid() {
        UserDto dto = new UserDto(
                null,
                "name",
                "email"
        );

        Set<ConstraintViolation<UserDto>> violations =
                validator.validate(dto, UserDto.Update.class);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Email должен быть валидным");
    }
}
