package ru.practicum.shareit.gateway.booking.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.gateway.booking.dto.BookingDto;
import ru.practicum.shareit.gateway.client.BaseClient;

@Service
@Slf4j
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> add(long userId, BookingDto bookingDto) {
        log.info("Отправка запроса создания резерва: {}, от пользователя {}", bookingDto, userId);
        return post("", userId, bookingDto);
    }

    public ResponseEntity<Object> update(long userId, long bookingId, boolean approved) {
        log.info("Отправка запроса обновления резерва: {}, от пользователя {}", bookingId, userId);
        return patch("/" + bookingId + "?approved=" + approved, userId, approved);
    }

    public ResponseEntity<Object> find(long userId, long bookingId) {
        log.info("Отправка запроса поиска резерва: {}, от пользователя {}", bookingId, userId);
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> findAllRequests(long userId, String state) {
        log.info("Отправка запроса поиска всех резервов");
        return get("?state=" + state, userId);
    }

    public ResponseEntity<Object> findAllByOwner(long userId, String state) {
        log.info("Отправка запроса поиска всех резервов пользователя {}", userId);
        return get("/owner?state=" + state, userId);
    }
}
