package ru.practicum.shareit.server.itemRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ru.practicum.shareit.server.exception.NotFoundException;
import ru.practicum.shareit.server.item.dao.ItemRepository;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.item.mapper.ItemMapper;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.request.dao.ItemRequestRepository;
import ru.practicum.shareit.server.request.dto.ItemRequestDto;
import ru.practicum.shareit.server.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.server.request.model.ItemRequest;
import ru.practicum.shareit.server.request.service.ItemRequestServiceDB;
import ru.practicum.shareit.server.user.dao.UserRepository;
import ru.practicum.shareit.server.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ItemRequestServiceDBTest {

    @InjectMocks
    private ItemRequestServiceDB service;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemRequestMapper itemRequestMapper;

    @Mock
    private ItemMapper itemMapper;

    private User user;
    private ItemRequest request;
    private ItemRequestDto requestDto;
    private Item item;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("john@mail.com");

        request = new ItemRequest();
        request.setId(1L);
        request.setDescription("Need item");
        request.setRequester(user);
        request.setCreated(LocalDateTime.now());

        requestDto = new ItemRequestDto();
        requestDto.setId(1L);
        requestDto.setDescription("Need item");

        item = new Item();
        item.setId(1L);
        item.setName("Item 1");
        item.setRequest(request);

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Item 1");
        itemDto.setRequestId(1L);
    }

    @Test
    void add_whenUserExists_thenSaveAndReturnDto() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRequestMapper.toEntity(requestDto, user)).thenReturn(request);
        when(itemRequestRepository.save(request)).thenReturn(request);
        when(itemRequestMapper.toDto(request)).thenReturn(requestDto);

        ItemRequestDto result = service.add(1L, requestDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(itemRequestRepository).save(request);
    }

    @Test
    void add_whenUserNotFound_thenThrow() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.add(1L, requestDto));
    }

    @Test
    void findAllOwnRequests_whenRequestsExist_thenReturnList() {
        when(itemRequestRepository.findByRequesterIdOrderByCreatedDesc(1L)).thenReturn(List.of(request));
        when(itemRepository.findByRequestIdIn(List.of(1L))).thenReturn(List.of(item));
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);
        when(itemRequestMapper.toDtoWithAnswers(request, List.of(itemDto))).thenReturn(requestDto);

        List<ItemRequestDto> result = service.findAllOwnRequests(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
    }

    @Test
    void findAllOwnRequests_whenNoRequests_thenReturnEmptyList() {
        when(itemRequestRepository.findByRequesterIdOrderByCreatedDesc(1L)).thenReturn(List.of());

        List<ItemRequestDto> result = service.findAllOwnRequests(1L);

        assertThat(result).isEmpty();
    }

    @Test
    void findAll_whenRequestsExist_thenReturnList() {
        when(itemRequestRepository.findByRequesterIdNotOrderByCreatedDesc(1L)).thenReturn(List.of(request));
        when(itemRequestMapper.toDto(request)).thenReturn(requestDto);

        List<ItemRequestDto> result = service.findAll(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    void findAll_whenNoRequests_thenReturnEmptyList() {
        when(itemRequestRepository.findByRequesterIdNotOrderByCreatedDesc(1L)).thenReturn(List.of());

        List<ItemRequestDto> result = service.findAll(1L);

        assertThat(result).isEmpty();
    }

    @Test
    void find_whenRequestExists_thenReturnDto() {
        when(itemRequestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(itemRepository.findByRequest_Id(1L)).thenReturn(List.of(item));
        when(itemMapper.toItemDtoForRequest(item)).thenReturn(itemDto);
        when(itemRequestMapper.toDtoWithAnswers(request, List.of(itemDto))).thenReturn(requestDto);

        ItemRequestDto result = service.find(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void find_whenRequestNotFound_thenThrow() {
        when(itemRequestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.find(1L));
    }
}

