package ru.practicum.shareit.gateway.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.gateway.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerialize() throws Exception {
        UserDto dto = new UserDto(
                null,
                "Иван",
                "ivan@example.com"
        );

        String json = objectMapper.writeValueAsString(dto);

        assertThat(json).contains("Иван");
        assertThat(json).contains("ivan@example.com");
        assertThat(json).doesNotContain("id");
    }

    @Test
    void testDeserialize() throws Exception {
        String json = "{\"name\":\"Мария\",\"email\":\"maria@example.com\"}";

        UserDto dto = objectMapper.readValue(json, UserDto.class);

        assertThat(dto.getName()).isEqualTo("Мария");
        assertThat(dto.getEmail()).isEqualTo("maria@example.com");
        assertThat(dto.getId()).isNull();
    }
}
