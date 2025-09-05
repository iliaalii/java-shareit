package ru.practicum.shareit.server.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.server.booking.controller.BookingController;
import ru.practicum.shareit.server.booking.dto.BookingDto;
import ru.practicum.shareit.server.booking.service.BookingService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        bookingDto.setItemId(1L);
    }

    @Test
    void testAddBooking() throws Exception {
        when(bookingService.add(any(BookingDto.class), anyLong())).thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .header(HEADER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookingDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.itemId").value(bookingDto.getItemId()));
    }

    @Test
    void testUpdateBooking() throws Exception {
        when(bookingService.update(anyLong(), anyLong(), anyBoolean())).thenReturn(bookingDto);

        mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header(HEADER_USER_ID, 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()));
    }

    @Test
    void testFindBooking() throws Exception {
        when(bookingService.find(anyLong(), anyLong())).thenReturn(bookingDto);

        mvc.perform(get("/bookings/{bookingId}", 1L)
                        .header(HEADER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemId").value(bookingDto.getItemId()));
    }

    @Test
    void testFindAllBookings() throws Exception {
        when(bookingService.findAll(anyLong(), anyString())).thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings")
                        .header(HEADER_USER_ID, 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testFindAllByOwner() throws Exception {
        when(bookingService.findAllByOwner(anyLong(), anyString())).thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings/owner")
                        .header(HEADER_USER_ID, 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
