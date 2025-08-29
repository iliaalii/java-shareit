package ru.practicum.shareit.server.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.server.booking.dto.BookingDto;
import ru.practicum.shareit.server.booking.service.BookingService;
import ru.practicum.shareit.server.item.dao.ItemRepository;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.user.dao.UserRepository;
import ru.practicum.shareit.server.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class BookingServiceTest {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    User user;
    Item item;

    @Test
    void addAndFindByOwner() {
        user = new User();
        user.setName("name");
        user.setEmail("test@mail.com");
        user = userRepository.save(user);

        item = new Item();
        item.setName("отвертка");
        item.setDescription("Инструмент");
        item.setAvailable(true);
        item.setOwner(user);
        item = itemRepository.save(item);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        bookingDto.setItemId(item.getId());
        bookingDto = bookingService.add(bookingDto, user.getId());

        assertThat(bookingDto.getId()).isNotNull();

        List<BookingDto> foundBooking = bookingService.findAllByOwner(user.getId(), "WAITING");

        assertThat(foundBooking).hasSize(1);
    }
}
