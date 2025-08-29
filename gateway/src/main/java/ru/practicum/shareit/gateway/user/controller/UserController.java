package ru.practicum.shareit.gateway.user.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.user.client.UserClient;
import ru.practicum.shareit.gateway.user.dto.UserDto;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> add(@Validated(UserDto.Create.class) @RequestBody UserDto userDto) {
        return userClient.add(userDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> find(@PathVariable @Positive long id) {
        return userClient.find(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findAllUser() {
        return userClient.findAllUsers();
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> update(@PathVariable @Positive Long id, @Validated(UserDto.Update.class) @RequestBody UserDto userDto) {
        return userClient.update(id, userDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> delete(@PathVariable @Positive Long id) {
        return userClient.delete(id);
    }
}
