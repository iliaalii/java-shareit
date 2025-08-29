package ru.practicum.shareit.gateway.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.gateway.item.dto.CommentDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerialize() throws Exception {
        CommentDto dto = new CommentDto(null, "test comment");

        String json = objectMapper.writeValueAsString(dto);

        assertThat(json).contains("test comment");
        assertThat(json).doesNotContain("id");
    }

    @Test
    void testDeserialize() throws Exception {
        String json = "{\"text\":\"test comment\"}";

        CommentDto dto = objectMapper.readValue(json, CommentDto.class);

        assertThat(dto.getText()).isEqualTo("test comment");
        assertThat(dto.getId()).isNull();
    }
}
