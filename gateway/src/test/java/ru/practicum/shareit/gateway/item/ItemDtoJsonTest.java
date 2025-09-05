package ru.practicum.shareit.gateway.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.gateway.item.dto.ItemDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerialize() throws Exception {
        ItemDto dto = new ItemDto(
                null,
                "Отвертка",
                "Инструмент",
                true,
                1L
        );

        String json = objectMapper.writeValueAsString(dto);

        assertThat(json).contains("Отвертка");
        assertThat(json).contains("Инструмент");
        assertThat(json).contains("\"available\":true");
        assertThat(json).contains("\"requestId\":1");
        assertThat(json).doesNotContain("id");
    }

    @Test
    void testDeserialize() throws Exception {
        String json = "{\"name\":\"Отвертка\",\"description\":\"Инструмент\",\"available\":false,\"requestId\":10}";

        ItemDto dto = objectMapper.readValue(json, ItemDto.class);

        assertThat(dto.getName()).isEqualTo("Отвертка");
        assertThat(dto.getDescription()).isEqualTo("Инструмент");
        assertThat(dto.getAvailable()).isFalse();
        assertThat(dto.getRequestId()).isEqualTo(10L);
        assertThat(dto.getId()).isNull();
    }
}
