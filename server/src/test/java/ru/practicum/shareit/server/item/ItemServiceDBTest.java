package ru.practicum.shareit.server.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.server.booking.dao.BookingRepository;
import ru.practicum.shareit.server.booking.dto.BookingDto;
import ru.practicum.shareit.server.booking.mapper.BookingMapper;
import ru.practicum.shareit.server.item.dao.CommentRepository;
import ru.practicum.shareit.server.item.dao.ItemRepository;
import ru.practicum.shareit.server.item.dto.CommentDto;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.item.mapper.CommentMapper;
import ru.practicum.shareit.server.item.mapper.ItemMapper;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.item.service.ItemServiceDB;
import ru.practicum.shareit.server.request.dao.ItemRequestRepository;
import ru.practicum.shareit.server.user.dao.UserRepository;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ItemServiceDBTest {

    @InjectMocks
    private ItemServiceDB itemService;

    @Mock
    private ItemRepository itemStorage;
    @Mock
    private UserRepository userStorage;
    @Mock
    private BookingRepository bookingStorage;
    @Mock
    private CommentRepository commentStorage;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private BookingMapper bookingMapper;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private ItemMapper itemMapper;

    private User user;
    private Item item;
    private ItemDto itemDto;
    private CommentDto commentDto;
    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("Alice");
        user.setEmail("alice@mail.com");

        item = new Item();
        item.setId(1L);
        item.setName("Item1");
        item.setOwner(user);

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Item1");

        commentDto = new CommentDto();
        commentDto.setText("Great item!");

        bookingDto = new BookingDto();
        bookingDto.setId(1L);
    }

    @Test
    void addItem_whenValid_thenSaved() {
        when(userStorage.findById(1L)).thenReturn(Optional.of(user));
        when(itemMapper.toItem(itemDto, user)).thenReturn(item);
        when(itemStorage.save(item)).thenReturn(item);
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        ItemDto result = itemService.add(1L, itemDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(itemStorage).save(item);
    }

    @Test
    void findItem_whenOwner_thenReturnItemWithBookings() {
        when(itemStorage.findById(1L)).thenReturn(Optional.of(item));
        when(commentStorage.findAllByItemIdOrderByCreatedDesc(1L)).thenReturn(List.of());
        when(bookingStorage.findFirstByItemIdAndEndBeforeOrderByEndDesc(eq(1L), any())).thenReturn(Optional.ofNullable(null));
        when(bookingStorage.findFirstByItemIdAndStartAfterOrderByStartAsc(eq(1L), any())).thenReturn(Optional.ofNullable(null));
        when(itemMapper.toItemDtoWithDatesAndComments(item, null, null, List.of())).thenReturn(itemDto);

        ItemDto result = itemService.find(1L, 1L);

        assertThat(result).isNotNull();
        verify(itemStorage).findById(1L);
    }

    @Test
    void findAllByUser_whenUserHasItems_thenReturnList() {
        when(userStorage.existsById(1L)).thenReturn(true);
        when(itemStorage.findByOwnerId(1L)).thenReturn(List.of(item));
        when(commentStorage.findAllByItemIdInOrderByCreatedDesc(List.of(1L))).thenReturn(List.of());
        when(bookingStorage.findLastBookings(any(), any())).thenReturn(List.of());
        when(bookingStorage.findNextBookings(any(), any())).thenReturn(List.of());
        when(itemMapper.toItemDtoWithDatesAndComments(eq(item), any(), any(), any())).thenReturn(itemDto);

        List<ItemDto> result = itemService.findAllByUser(1L);

        assertThat(result).hasSize(1);
        verify(itemStorage).findByOwnerId(1L);
    }

    @Test
    void updateItem_whenExists_thenUpdated() {
        when(userStorage.existsById(1L)).thenReturn(true);
        when(itemStorage.findById(1L)).thenReturn(Optional.of(item));
        when(itemStorage.save(item)).thenReturn(item);
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        ItemDto result = itemService.update(1L, 1L, itemDto);

        assertThat(result).isNotNull();
        verify(itemMapper).updateItem(item, itemDto);
        verify(itemStorage).save(item);
    }

    @Test
    void searchItems_whenTextBlank_thenReturnEmpty() {
        when(userStorage.existsById(1L)).thenReturn(true);

        List<ItemDto> result = itemService.search(1L, " ");

        assertThat(result).isEmpty();
    }

    @Test
    void addComment_whenValid_thenSaved() {
        when(userStorage.findById(1L)).thenReturn(Optional.of(user));
        when(itemStorage.findById(1L)).thenReturn(Optional.of(item));
        when(bookingStorage.existsByBookerIdAndItemIdAndEndBefore(eq(1L), eq(1L), any(LocalDateTime.class))).thenReturn(true);
        when(commentMapper.toComment(any(), eq(user), eq(item))).thenAnswer(invocation -> {
            var dto = invocation.getArgument(0, CommentDto.class);
            var comment = new ru.practicum.shareit.server.item.model.Comment();
            comment.setText(dto.getText());
            comment.setItem(item);
            comment.setAuthor(user);
            return comment;
        });
        when(commentStorage.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(commentMapper.toCommentDto(any())).thenAnswer(invocation -> {
            var comment = invocation.getArgument(0, ru.practicum.shareit.server.item.model.Comment.class);
            var dto = new CommentDto();
            dto.setText(comment.getText());
            return dto;
        });

        CommentDto result = itemService.addComment(1L, 1L, commentDto);

        assertThat(result).isNotNull();
        assertThat(result.getText()).isEqualTo("Great item!");
        verify(commentStorage).save(any());
    }

    @Test
    void addComment_whenNoPastBooking_thenThrowsException() {
        when(userStorage.findById(1L)).thenReturn(Optional.of(user));
        when(itemStorage.findById(1L)).thenReturn(Optional.of(item));
        when(bookingStorage.existsByBookerIdAndItemIdAndEndBefore(eq(1L), eq(1L), any(LocalDateTime.class))).thenReturn(false);

        assertThatThrownBy(() -> itemService.addComment(1L, 1L, commentDto))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Пользователь не может оставить комментарий");
    }
}
