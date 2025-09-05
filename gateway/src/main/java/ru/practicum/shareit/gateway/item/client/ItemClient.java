package ru.practicum.shareit.gateway.item.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.gateway.client.BaseClient;
import ru.practicum.shareit.gateway.item.dto.CommentDto;
import ru.practicum.shareit.gateway.item.dto.ItemDto;

@Service
@Slf4j
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> add(long userId, ItemDto itemDto) {
        log.info("Отправка запроса создания вещи: {}, от пользователя {}", itemDto, userId);
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> find(long userId, long itemId) {
        log.info("Отправка запроса поиска вещи: {}, от пользователя {}", itemId, userId);
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> findAllByUser(long userId) {
        log.info("Отправка запроса поиска вещей пользователя {}", userId);
        return get("", userId);
    }

    public ResponseEntity<Object> update(long userId, long itemId, ItemDto itemDto) {
        log.info("Отправка запроса обновления вещи: {}, на {}", itemId, itemDto);
        return patch("/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> search(long userId, String text) {
        log.info("Отправка запроса поиска вещи от пользователя {}, по ключевым словам {}", userId, text);
        return get("/search?text=" + text, userId);
    }

    public ResponseEntity<Object> addComment(long userId, long itemId, CommentDto comment) {
        log.info("Отправка запроса добавления комментария {} вещи {} от пользователя: {}", comment, itemId, userId);
        return post("/" + itemId + "/comment", userId, comment);
    }
}
