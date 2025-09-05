package ru.practicum.shareit.gateway.request.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.gateway.client.BaseClient;
import ru.practicum.shareit.gateway.request.dto.ItemRequestDto;

@Service
@Slf4j
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> add(long userId, ItemRequestDto itemRequestDto) {
        log.info("Отправка запроса создания запроса вещи: {}, от пользователя: {}", itemRequestDto, userId);
        return post("", userId, itemRequestDto);
    }

    public ResponseEntity<Object> findAllOwnRequests(long userId) {
        log.info("Отправка запроса поиска всех запросов вещей от пользователя: {}", userId);
        return get("", userId);
    }

    public ResponseEntity<Object> findAll(long userId) {
        log.info("Отправка запроса поиска всех запросов вещей");
        return get("/all", userId);
    }

    public ResponseEntity<Object> find(long requestId) {
        log.info("Отправка запроса поиска всех запросов вещей");
        return get("/" + requestId);
    }
}
