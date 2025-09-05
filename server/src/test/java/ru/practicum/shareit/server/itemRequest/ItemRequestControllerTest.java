package ru.practicum.shareit.server.itemRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.server.request.controller.ItemRequestController;
import ru.practicum.shareit.server.request.dto.ItemRequestDto;
import ru.practicum.shareit.server.request.service.ItemRequestService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemRequestService itemRequestService;

    private ItemRequestDto requestDto;

    @BeforeEach
    void setup() {
        requestDto = new ItemRequestDto();
        requestDto.setId(1L);
        requestDto.setDescription("Описание запроса");
    }

    @Test
    void testAddRequest() throws Exception {
        when(itemRequestService.add(anyLong(), any(ItemRequestDto.class)))
                .thenReturn(requestDto);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(requestDto.getId()))
                .andExpect(jsonPath("$.description").value(requestDto.getDescription()));
    }

    @Test
    void testFindAllOwnRequests() throws Exception {
        when(itemRequestService.findAllOwnRequests(anyLong()))
                .thenReturn(List.of(requestDto));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(requestDto.getId()));
    }

    @Test
    void testFindAllRequests() throws Exception {
        when(itemRequestService.findAll(anyLong()))
                .thenReturn(List.of(requestDto));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].description").value(requestDto.getDescription()));
    }

    @Test
    void testFindRequestById() throws Exception {
        when(itemRequestService.find(anyLong()))
                .thenReturn(requestDto);

        mvc.perform(get("/requests/{requestId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestDto.getId()))
                .andExpect(jsonPath("$.description").value(requestDto.getDescription()));
    }
}
