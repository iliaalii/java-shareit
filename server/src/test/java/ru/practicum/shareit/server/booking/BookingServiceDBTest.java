package ru.practicum.shareit.server.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ru.practicum.shareit.server.booking.dao.BookingRepository;
import ru.practicum.shareit.server.booking.dto.BookingDto;
import ru.practicum.shareit.server.booking.mapper.BookingMapper;
import ru.practicum.shareit.server.booking.model.Booking;
import ru.practicum.shareit.server.booking.model.Status;
import ru.practicum.shareit.server.booking.service.BookingServiceDB;
import ru.practicum.shareit.server.exception.AvailabilityException;
import ru.practicum.shareit.server.exception.NotFoundException;
import ru.practicum.shareit.server.exception.ValidationException;
import ru.practicum.shareit.server.item.dao.ItemRepository;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.user.dao.UserRepository;
import ru.practicum.shareit.server.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceDBTest {

    @Mock
    private BookingRepository bookingStorage;
    @Mock
    private ItemRepository itemStorage;
    @Mock
    private UserRepository userStorage;
    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingServiceDB bookingService;

    private User user;
    private Item item;
    private Booking booking;
    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("email@mail.com");

        item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(user);

        booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(Status.WAITING);

        bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        bookingDto.setStatus("WAITING");
    }

    @Test
    void add_shouldThrow_ifUserNotFound() {
        when(userStorage.findById(user.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.add(bookingDto, user.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Пользователь с id");
    }

    @Test
    void add_shouldThrow_ifItemNotFound() {
        when(userStorage.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemStorage.findById(item.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.add(bookingDto, user.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Вещь с id");
    }

    @Test
    void add_shouldThrow_ifItemNotAvailable() {
        item.setAvailable(false);
        when(userStorage.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemStorage.findById(item.getId())).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> bookingService.add(bookingDto, user.getId()))
                .isInstanceOf(AvailabilityException.class)
                .hasMessageContaining("не доступна");
    }

    @Test
    void update_shouldApproveBooking_ifOwnerApproves() {
        booking.setStatus(Status.WAITING);
        when(bookingStorage.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(userStorage.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookingStorage.save(any())).thenReturn(booking);
        when(bookingMapper.toDTO(any())).thenReturn(bookingDto);

        BookingDto result = bookingService.update(user.getId(), booking.getId(), true);

        assertThat(result).isNotNull();
        assertThat(booking.getStatus()).isEqualTo(Status.APPROVED);
    }

    @Test
    void update_shouldThrow_ifNotOwner() {
        User otherUser = new User();
        otherUser.setId(99L);
        otherUser.setName("other");
        otherUser.setEmail("other@mail.com");
        when(bookingStorage.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(userStorage.findById(otherUser.getId())).thenReturn(Optional.of(otherUser));

        assertThatThrownBy(() -> bookingService.update(otherUser.getId(), booking.getId(), true))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("хозяин вещи");
    }

    @Test
    void find_shouldReturnBooking_ifExists() {
        when(bookingStorage.findByIdAndUserIsBookerOrOwner(booking.getId(), user.getId()))
                .thenReturn(Optional.of(booking));
        when(bookingMapper.toDTO(booking)).thenReturn(bookingDto);

        BookingDto result = bookingService.find(user.getId(), booking.getId());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void find_shouldThrow_ifNotFound() {
        when(bookingStorage.findByIdAndUserIsBookerOrOwner(booking.getId(), user.getId()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.find(user.getId(), booking.getId()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void dateValidation_shouldThrow_ifStartAfterEnd() {
        bookingDto.setStart(LocalDateTime.now().plusDays(3));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        assertThatThrownBy(() -> bookingService.add(bookingDto, user.getId()))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("начала резерва не может быть после конца");
    }

    @Test
    void add_whenValidBooking_thenSaved() {
        when(userStorage.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemStorage.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingMapper.toBooking(any(),any(),any())).thenReturn(booking);
        when(bookingStorage.save(any())).thenReturn(booking);
        when(bookingMapper.toDTO(any())).thenReturn(bookingDto);

        BookingDto result = bookingService.add(bookingDto, user.getId());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(bookingStorage).save(any());
    }

    @Test
    void update_whenBookingExists_thenUpdated() {
        Booking approved = new Booking();
        approved.setStart(booking.getStart());
        approved.setEnd(booking.getEnd());
        approved.setItem(item);
        approved.setBooker(user);
        approved.setStatus(Status.APPROVED);

        when(userStorage.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(bookingStorage.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingStorage.save(any())).thenReturn(approved);
        BookingDto dto = new BookingDto();
        dto.setId(1L);
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setItemId(item.getId());
        dto.setStatus("APPROVED");
        when(bookingMapper.toDTO(any())).thenReturn(dto);

        BookingDto result = bookingService.update(user.getId(), booking.getId(), true);

        assertThat(result.getStatus()).isEqualTo("APPROVED");
        verify(bookingStorage).save(any());
    }

    @Test
    void findAll_whenUserHasBookings_thenReturnList() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setItemId(item.getId());
        bookingDto.setStatus("APPROVED");

        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingStorage.findByBookerIdOrderByStartDesc(anyLong()))
                .thenReturn(List.of(booking));
        when(bookingMapper.toDTO(any())).thenReturn(bookingDto);

        List<BookingDto> result = bookingService.findAll(1L, "ALL");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        bookingService.findAll(1L, "CURRENT");
        bookingService.findAll(1L, "PAST");
        bookingService.findAll(1L, "FUTURE");
        bookingService.findAll(1L, "WAITING");
        bookingService.findAll(1L, "REJECTED");
    }

    @Test
    void findAllByOwner_whenOwnerHasBookings_thenReturnList() {
        when(userStorage.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookingMapper.toDTO(any())).thenReturn(bookingDto);
        when(bookingStorage.findByItemOwnerIdOrderByStartDesc(anyLong()))
                .thenReturn(List.of(booking));
        List<BookingDto> result = bookingService.findAllByOwner(user.getId(), "ALL");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getItemId()).isEqualTo(item.getId());
        bookingService.findAllByOwner(1L, "CURRENT");
        bookingService.findAllByOwner(1L, "PAST");
        bookingService.findAllByOwner(1L, "FUTURE");
        bookingService.findAllByOwner(1L, "WAITING");
        bookingService.findAllByOwner(1L, "REJECTED");
    }
}
