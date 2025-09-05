package ru.practicum.shareit.server.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorHandlerTest {

    private final ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void handleNotFound_shouldReturnErrorResponse() {
        NotFoundException ex = new NotFoundException("User not found");

        ErrorHandler.ErrorResponse response = errorHandler.handleNotFound(ex);

        assertThat(response.error()).isEqualTo("Запрашиваемый объект не найден.");
        assertThat(response.description()).isEqualTo("User not found");
    }

    @Test
    void handleConflict_shouldReturnErrorResponse() {
        ConflictException ex = new ConflictException("Email already exists");

        ErrorHandler.ErrorResponse response = errorHandler.handleConflictException(ex);

        assertThat(response.error()).isEqualTo("Конфликт при добавлении");
        assertThat(response.description()).isEqualTo("Email already exists");
    }

    @Test
    void handleUnexpected_shouldReturnErrorResponse() {
        Exception ex = new Exception("Unexpected");

        ErrorHandler.ErrorResponse response = errorHandler.handleUnexpectedExceptions(ex);

        assertThat(response.error()).isEqualTo("Внутренняя ошибка сервера");
        assertThat(response.description()).isEqualTo("Unexpected");
    }

    @Test
    void handleAvailability_shouldReturnErrorResponse() {
        AvailabilityException ex = new AvailabilityException("Item not available");

        ErrorHandler.ErrorResponse response = errorHandler.handleAvailabilityException(ex);

        assertThat(response.error()).isEqualTo("Ошибка доступности вещи: ");
        assertThat(response.description()).isEqualTo("Item not available");
    }

    @Test
    void handleValidation_shouldReturnErrorResponse() {
        ValidationException ex = new ValidationException("Email invalid");

        ErrorHandler.ErrorResponse response = errorHandler.handleValidationException(ex);

        assertThat(response.error()).isEqualTo("Ошибка валидации: ");
        assertThat(response.description()).isEqualTo("Email invalid");
    }
}

