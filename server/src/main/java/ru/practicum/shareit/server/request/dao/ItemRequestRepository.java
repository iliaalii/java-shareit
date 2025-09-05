package ru.practicum.shareit.server.request.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.server.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByRequesterIdOrderByCreatedDesc(Long requesterId);

    List<ItemRequest> findByRequesterIdNotOrderByCreatedDesc(Long requesterId);
}
