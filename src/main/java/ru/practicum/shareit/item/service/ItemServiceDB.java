package ru.practicum.shareit.item.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ItemServiceDB implements ItemService {
    private final ItemRepository itemStorage;
    private final UserRepository userStorage;
    private final BookingRepository bookingStorage;
    private final CommentRepository commentStorage;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto add(Long userId, ItemDto itemDto) {
        log.info("Обработка запроса на добавление вещи от пользователя {}", userId);
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        Item item = itemMapper.toItem(itemDto, user);
        return itemMapper.toItemDto(itemStorage.save(item));
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto find(Long userId, Long itemId) {
        log.info("Обработка запроса на поиск вещи");
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет с id " + itemId + " не найден"));
        LocalDateTime now = LocalDateTime.now();
        List<CommentDto> comments = commentStorage.findAllByItemIdOrderByCreatedDesc(item.getId())
                .stream()
                .map(commentMapper::toCommentDto)
                .toList();
        BookingDto lastBooking = null;
        BookingDto nextBooking = null;

        if (item.getOwner().getId().equals(userId)) {
            lastBooking = bookingStorage
                    .findFirstByItemIdAndEndBeforeOrderByEndDesc(item.getId(), now)
                    .map(bookingMapper::toDTO)
                    .orElse(null);

            nextBooking = bookingStorage
                    .findFirstByItemIdAndStartAfterOrderByStartAsc(item.getId(), now)
                    .map(bookingMapper::toDTO)
                    .orElse(null);
        }
        return itemMapper.toItemDtoWithDatesAndComments(item, lastBooking, nextBooking, comments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> findAllByUser(Long userId) {
        log.info("Обработка запроса на поиск вещей");
        if (!userStorage.existsById(userId)) throw new NotFoundException("Пользователь с id " + userId + " не найден");

        List<Item> items = itemStorage.findByOwnerId(userId);

        if (items.isEmpty()) {
            return List.of();
        }

        List<Long> itemIds = items.stream()
                .map(Item::getId)
                .toList();
        List<Comment> comments = commentStorage.findAllByItemIdInOrderByCreatedDesc(itemIds);

        Map<Long, List<Comment>> commentsByItem = comments.stream()
                .collect(Collectors.groupingBy(c -> c.getItem().getId()));

        LocalDateTime now = LocalDateTime.now();

        List<Booking> lastBookings = bookingStorage.findLastBookings(itemIds, now);
        Map<Long, BookingDto> lastByItem = lastBookings.stream()
                .collect(Collectors.toMap(
                        b -> b.getItem().getId(),
                        bookingMapper::toDTO,
                        (b1, b2) -> b1
                ));

        List<Booking> nextBookings = bookingStorage.findNextBookings(itemIds, now);
        Map<Long, BookingDto> nextByItem = nextBookings.stream()
                .collect(Collectors.toMap(
                        b -> b.getItem().getId(),
                        bookingMapper::toDTO,
                        (b1, b2) -> b1
                ));

        return items.stream()
                .map(item -> {
                    BookingDto lastBooking = null;
                    BookingDto nextBooking = null;

                    if (item.getOwner().getId().equals(userId)) {
                        lastBooking = lastByItem.get(item.getId());
                        nextBooking = nextByItem.get(item.getId());
                    }

                    List<CommentDto> commentDtos = commentsByItem
                            .getOrDefault(item.getId(), List.of())
                            .stream()
                            .map(commentMapper::toCommentDto)
                            .toList();

                    return itemMapper.toItemDtoWithDatesAndComments(item, lastBooking, nextBooking, commentDtos);
                })
                .toList();
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        log.info("Обработка запроса на обновление вещи, от пользователя {}", userId);
        if (userStorage.existsById(userId)) {
            Item item = itemStorage.findById(itemId)
                    .orElseThrow(() -> new NotFoundException("Предмет с id " + itemId + " не найден"));
            ;
            itemMapper.updateItem(item, itemDto);
            return itemMapper.toItemDto(itemStorage.save(item));
        } else {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> search(Long userId, String text) {
        log.info("Обработка запроса на поиск пользователем {}", userId);
        if (userStorage.existsById(userId)) {
            if (text == null || text.isBlank()) {
                return List.of();
            }
            return itemStorage.searchAvailableItems(text).stream()
                    .map(itemMapper::toItemDto)
                    .toList();
        } else {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        log.info("Обработка запроса на добавление комментария от пользователя {}", userId);
        User author = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));

        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена"));

        LocalDateTime now = LocalDateTime.now();
        boolean hasPastBooking = bookingStorage
                .existsByBookerIdAndItemIdAndEndBefore(userId, itemId, now);
        if (!hasPastBooking) {
            throw new ValidationException("Пользователь не может оставить комментарий, " +
                    "так как он не арендовал вещь или аренда не завершена");
        }
        Comment comment = commentMapper.toComment(commentDto, author, item);
        comment.setCreated(now);
        return commentMapper.toCommentDto(commentStorage.save(comment));
    }
}
