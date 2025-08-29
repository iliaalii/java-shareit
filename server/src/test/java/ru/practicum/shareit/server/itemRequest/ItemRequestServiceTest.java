package ru.practicum.shareit.server.itemRequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.server.request.dto.ItemRequestDto;
import ru.practicum.shareit.server.request.service.ItemRequestService;
import ru.practicum.shareit.server.user.dao.UserRepository;
import ru.practicum.shareit.server.user.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ItemRequestServiceTest {
    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private UserRepository userRepository;

    User user;
    ItemRequestDto itemRequestDto;

    @Test
    void addAndFind() {
        user = new User();
        user.setName("name");
        user.setEmail("test@mail.com");
        user = userRepository.save(user);

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("запрос инструментов");

        itemRequestDto = itemRequestService.add(user.getId(), itemRequestDto);
        assertThat(itemRequestDto.getId()).isNotNull();

        List<ItemRequestDto> foundRequests = itemRequestService.findAllOwnRequests(user.getId());
        assertThat(foundRequests).hasSize(1);
    }
}
