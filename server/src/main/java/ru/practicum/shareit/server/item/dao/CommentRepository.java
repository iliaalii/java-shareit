package ru.practicum.shareit.server.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.server.item.model.Comment;

import java.util.Collection;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByItemIdOrderByCreatedDesc(Long itemId);

    List<Comment> findAllByItemIdInOrderByCreatedDesc(Collection<Long> itemIds);
}
