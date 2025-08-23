package ru.practicum.shareit.booking.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.AvailabilityException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BookingServiceDB implements BookingService {
    private final BookingRepository bookingStorage;
    private final ItemRepository itemStorage;
    private final UserRepository userStorage;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto add(BookingDto bookingDto, Long userId) {
        log.info("Обработка запросна на добавление нового бронирования");
        dateValidation(bookingDto);
        User booker = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        Item item = itemStorage.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь с id " + bookingDto.getItemId() + " не найдена"));
        if (!item.isAvailable()) throw new AvailabilityException("Вещь не доступна для резерва");

        Booking booking = bookingMapper.toBooking(bookingDto, item, booker);
        booking.setStatus(Status.WAITING);
        return bookingMapper.toDTO(bookingStorage.save(booking));
    }

    @Override
    public BookingDto update(Long userId, Long bookingId, boolean approved) {
        log.info("Обработка запроса изменения статуса бронирования");
        Booking booking = bookingStorage.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id " + bookingId + " не найдено"));
        User owner = userStorage.findById(userId)
                .orElseThrow(() -> new ValidationException("Пользователь не найден"));
        Item item = booking.getItem();
        if (!item.getOwner().equals(owner)) {
            throw new ValidationException("Подтверждать бронирование может только хозяин вещи");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
            item.setAvailable(false);
            itemStorage.save(item);
        } else {
            booking.setStatus(Status.REJECTED);
        }

        return bookingMapper.toDTO(bookingStorage.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto find(Long userId, Long bookingId) {
        log.info("Обработка запроса поиска бронирования");
        Booking booking = bookingStorage.findByIdAndUserIsBookerOrOwner(bookingId, userId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id " + bookingId + " не найдено"));
        return bookingMapper.toDTO(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> findAll(Long userId, String state) {
        log.info("Обработка запроса поиска всех бронирований пользователя, по состоянию: {}", state);
        LocalDateTime now = LocalDateTime.now();
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        return switch (state) {
            case "CURRENT" -> bookingStorage.findCurrentBookings(userId, now)
                    .stream().map(bookingMapper::toDTO).toList();
            case "PAST" -> bookingStorage.findPastBookings(userId, now)
                    .stream().map(bookingMapper::toDTO).toList();
            case "FUTURE" -> bookingStorage.findFutureBookings(userId, now)
                    .stream().map(bookingMapper::toDTO).toList();
            case "WAITING" -> bookingStorage.findByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING)
                    .stream().map(bookingMapper::toDTO).toList();
            case "REJECTED" -> bookingStorage.findByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED)
                    .stream().map(bookingMapper::toDTO).toList();
            case "ALL" -> bookingStorage.findByBookerIdOrderByStartDesc(userId)
                    .stream().map(bookingMapper::toDTO).toList();
            default -> throw new ValidationException("Неверный запрос по state");
        };
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> findAllByOwner(Long userId, String state) {
        log.info("Обработка запроса поиска всех бронирований владельца, по состоянию: {}", state);
        LocalDateTime now = LocalDateTime.now();
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        return switch (state.toUpperCase()) {
            case "CURRENT" -> bookingStorage.findCurrentByOwner(userId, now)
                    .stream().map(bookingMapper::toDTO).toList();
            case "PAST" -> bookingStorage.findPastByOwner(userId, now)
                    .stream().map(bookingMapper::toDTO).toList();
            case "FUTURE" -> bookingStorage.findFutureByOwner(userId, now)
                    .stream().map(bookingMapper::toDTO).toList();
            case "WAITING" -> bookingStorage.findByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING)
                    .stream().map(bookingMapper::toDTO).toList();
            case "REJECTED" -> bookingStorage.findByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.REJECTED)
                    .stream().map(bookingMapper::toDTO).toList();
            case "ALL" -> bookingStorage.findByItemOwnerIdOrderByStartDesc(userId)
                    .stream().map(bookingMapper::toDTO).toList();
            default -> throw new ValidationException("Неверный запрос по state");
        };
    }

    private void dateValidation(BookingDto booking) {
        if (booking.getStart().isAfter(booking.getEnd())) {
            throw new ValidationException("Дата начала резерва не может быть после конца");
        } else if (booking.getStart().isEqual(booking.getEnd())) {
            throw new ValidationException("Начало и конец бронирования не должны совпадать");
        } else if (booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Конец резерва не может быть в прошлом");
        } else if (booking.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Старт резерва не может быть в прошлом");
        }
    }
}
