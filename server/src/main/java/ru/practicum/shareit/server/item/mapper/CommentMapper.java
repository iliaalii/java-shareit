package ru.practicum.shareit.server.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ru.practicum.shareit.server.item.dto.CommentDto;
import ru.practicum.shareit.server.item.model.Comment;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.user.model.User;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "authorName", source = "author.name")
    CommentDto toCommentDto(Comment comment);

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "item", source = "item")
    Comment toComment(CommentDto dto, User author, Item item);
}
