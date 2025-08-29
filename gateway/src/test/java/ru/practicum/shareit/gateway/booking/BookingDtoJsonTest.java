package ru.practicum.shareit.gateway.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.gateway.booking.dto.BookingDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingDtoJsonTest {
    @Autowired
    private JacksonTester<BookingDto> json;

    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(LocalDateTime.of(2030, 12, 1, 12, 0, 0));
        bookingDto.setEnd(LocalDateTime.of(2030, 12, 1, 12, 0, 0));
        bookingDto.setItemId(1L);
    }

    @Test
    void testSerialize() throws Exception {

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo("2030-12-01T12:00:00");
        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo("2030-12-01T12:00:00");
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
    }

    @Test
    void testDeserialize() throws Exception {
        String jsonContent = "{\"id\":1,\"start\":\"2025-08-30T12:00:00\",\"end\":\"2025-08-31T12:00:00\",\"itemId\":1}";

        BookingDto parsed = json.parseObject(jsonContent);

        assertThat(parsed.getId()).isEqualTo(1L);
        assertThat(parsed.getStart()).isEqualTo(LocalDateTime.of(2025, 8, 30, 12, 0, 0));
        assertThat(parsed.getEnd()).isEqualTo(LocalDateTime.of(2025, 8, 31, 12, 0, 0));
        assertThat(parsed.getItemId()).isEqualTo(1L);
    }
}
