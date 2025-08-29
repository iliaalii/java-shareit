package ru.practicum.shareit.server.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.server.item.dao.ItemRepository;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.item.service.ItemService;
import ru.practicum.shareit.server.user.dao.UserRepository;
import ru.practicum.shareit.server.user.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    User user;
    Item item;

    @BeforeEach
    void setup() {
        user = new User();
        user.setName("name");
        user.setEmail("test@mail.com");

        item = new Item();
        item.setName("отвертка");
        item.setDescription("Инструмент");
        item.setAvailable(true);
    }

    @Test
    void testFindUserItems() {
        user = userRepository.save(user);

        item.setOwner(user);
        itemRepository.save(item);

        List<ItemDto> items = itemService.findAllByUser(user.getId());

        assertThat(items).hasSize(1);
        assertThat(items.get(0).getName()).isEqualTo("отвертка");
    }

    @Test
    void testSearch() {
        user = userRepository.save(user);
        System.out.println(user);

        item.setOwner(user);
        itemRepository.save(item);

        item = new Item();
        item.setName("ключ");
        item.setDescription("Инструмент");
        item.setAvailable(true);
        item.setOwner(user);
        itemRepository.save(item);

        item = new Item();
        item.setName("ключ");
        item.setDescription("запчасти");
        item.setAvailable(true);
        item.setOwner(user);
        itemRepository.save(item);

        user = new User();
        user.setName("name");
        user.setEmail("other@mail.com");

        user = userRepository.save(user);
        System.out.println(user);
        List<ItemDto> items = itemService.search(user.getId(), "Инструмент");
        assertThat(items).hasSize(2);
        assertThat(items.get(0).getName()).isEqualTo("отвертка");
        assertThat(items.get(1).getName()).isEqualTo("ключ");
    }
}
