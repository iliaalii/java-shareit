package ru.practicum.shareit.gateway.user.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.gateway.client.BaseClient;
import ru.practicum.shareit.gateway.user.dto.UserDto;

@Service
@Slf4j
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> add(UserDto userDto) {
        log.info("Отправка запроса создания пользователя: {}", userDto);
        return post("", userDto);
    }

    public ResponseEntity<Object> find(long id) {
        log.info("Отправка запроса поиска пользователя: {}", id);
        return get("/" + id);
    }

    public ResponseEntity<Object> findAllUsers() {
        log.info("Отправка запроса поиска всех пользователей");
        return get("");
    }

    public ResponseEntity<Object> update(long id, UserDto userDto) {
        log.info("Отправка запроса обновления пользователя: {}, на {}", id, userDto);
        return patch("/" + id, userDto);
    }

    public ResponseEntity<Object> delete(long id) {
        log.info("Отправка запроса удаления пользователя: {}", id);
        return delete("/" + id);
    }
}
