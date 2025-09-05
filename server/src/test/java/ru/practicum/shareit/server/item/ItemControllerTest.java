package ru.practicum.shareit.server.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.server.item.controller.ItemController;
import ru.practicum.shareit.server.item.dto.CommentDto;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.item.service.ItemService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper mapper;

    private ItemDto itemDto;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto();
        itemDto.setName("Отвертка");
        itemDto.setDescription("Инструмент");
        itemDto.setAvailable(true);

        commentDto = new CommentDto();
        commentDto.setText("Хорошая вещь");
    }

    @Test
    void testAddItem() throws Exception {
        when(itemService.add(anyLong(), any(ItemDto.class))).thenReturn(itemDto);

        mvc.perform(post("/items")
                        .header(HEADER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()));
    }

    @Test
    void testFindItem() throws Exception {
        when(itemService.find(anyLong(), anyLong())).thenReturn(itemDto);

        mvc.perform(get("/items/{itemId}", 1L)
                        .header(HEADER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(itemDto.getName()));
    }

    @Test
    void testFindAllByUser() throws Exception {
        when(itemService.findAllByUser(anyLong())).thenReturn(List.of(itemDto));

        mvc.perform(get("/items")
                        .header(HEADER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testUpdateItem() throws Exception {
        when(itemService.update(anyLong(), anyLong(), any(ItemDto.class))).thenReturn(itemDto);

        mvc.perform(patch("/items/{itemId}", 1L)
                        .header(HEADER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()));
    }

    @Test
    void testSearch() throws Exception {
        when(itemService.search(anyLong(), any())).thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search")
                        .header(HEADER_USER_ID, 1L)
                        .param("text", "Отвертка"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testAddComment() throws Exception {
        when(itemService.addComment(anyLong(), anyLong(), any(CommentDto.class))).thenReturn(commentDto);

        mvc.perform(post("/items/{itemId}/comment", 1L)
                        .header(HEADER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(commentDto.getText()));
    }
}
