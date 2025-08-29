package ru.practicum.shareit.server.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.request.dto.ItemRequestDto;
import ru.practicum.shareit.server.request.model.ItemRequest;
import ru.practicum.shareit.server.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "requester", source = "requester")
    ItemRequest toEntity(ItemRequestDto dto, User requester);

    @Mapping(target = "items", ignore = true)
    ItemRequestDto toDto(ItemRequest entity);

    @Mapping(target = "items", source = "items")
    ItemRequestDto toDtoWithAnswers(ItemRequest entity, List<ItemDto> items);
}
