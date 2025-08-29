package ru.practicum.shareit.gateway.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.gateway.request.dto.ItemRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerialize() throws Exception {
        ItemRequestDto dto = new ItemRequestDto(
                null,
                "Нужен молоток"
        );

        String json = objectMapper.writeValueAsString(dto);

        assertThat(json).contains("Нужен молоток");
        assertThat(json).doesNotContain("id");
    }

    @Test
    void testDeserialize() throws Exception {
        String json = "{\"description\":\"Хочу арендовать дрель\"}";

        ItemRequestDto dto = objectMapper.readValue(json, ItemRequestDto.class);

        assertThat(dto.getDescription()).isEqualTo("Хочу арендовать дрель");
        assertThat(dto.getId()).isNull();
    }
}
