package ru.practicum.shareit.item.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
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

    @Override
    public ItemDto add(Long userId, ItemDto itemDto) {
        log.info("Обработка запроса на добавление вещи от пользователя {}", userId);
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        Item item = ItemMapper.toItem(itemDto, user);
        return ItemMapper.toItemDto(itemStorage.save(item));
    }

    @Override
    public ItemDto find(Long userId, Long itemId) {
        log.info("Обработка запроса на поиск вещи");
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет с id " + itemId + " не найден"));
        LocalDateTime now = LocalDateTime.now();
        List<CommentDto> comments = commentStorage.findAllByItemIdOrderByCreatedDesc(item.getId())
                .stream()
                .map(CommentMapper::toCommentDto)
                .toList();
        BookingDto lastBooking = null;
        BookingDto nextBooking = null;

        if (item.getOwner().getId().equals(userId)) {
            lastBooking = bookingStorage
                    .findFirstByItemIdAndEndBeforeOrderByEndDesc(item.getId(), now)
                    .map(BookingMapper::toBookingDto)
                    .orElse(null);

            nextBooking = bookingStorage
                    .findFirstByItemIdAndStartAfterOrderByStartAsc(item.getId(), now)
                    .map(BookingMapper::toBookingDto)
                    .orElse(null);
        }
        return ItemMapper.toItemDtoWithDatesAndComments(item, lastBooking, nextBooking, comments);
    }

    @Override
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
        return items.stream()
                .map(item -> {
                    BookingDto lastBooking = null;
                    BookingDto nextBooking = null;

                    if (item.getOwner().getId().equals(userId)) {
                        lastBooking = bookingStorage
                                .findFirstByItemIdAndEndBeforeOrderByEndDesc(item.getId(), now)
                                .map(BookingMapper::toBookingDto)
                                .orElse(null);

                        nextBooking = bookingStorage
                                .findFirstByItemIdAndStartAfterOrderByStartAsc(item.getId(), now)
                                .map(BookingMapper::toBookingDto)
                                .orElse(null);
                    }

                    List<CommentDto> commentDtos = commentsByItem
                            .getOrDefault(item.getId(), List.of())
                            .stream()
                            .map(CommentMapper::toCommentDto)
                            .toList();

                    return ItemMapper.toItemDtoWithDatesAndComments(item, lastBooking, nextBooking, commentDtos);
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
            ItemMapper.updateItem(item, itemDto);
            return ItemMapper.toItemDto(itemStorage.save(item));
        } else {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
    }

    @Override
    public List<ItemDto> search(Long userId, String text) {
        log.info("Обработка запроса на поиск пользователем {}", userId);
        if (userStorage.existsById(userId)) {
            if (text == null || text.isBlank()) {
                return List.of();
            }
            return itemStorage.searchAvailableItems(text).stream()
                    .map(ItemMapper::toItemDto)
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
        Comment comment = CommentMapper.toComment(commentDto, author, item);
        comment.setCreated(now);
        return CommentMapper.toCommentDto(commentStorage.save(comment));
    }
}
