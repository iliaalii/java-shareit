package ru.practicum.shareit.booking.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = {"id"})
public class Booking {
    Integer id;
    LocalDateTime start;
    LocalDateTime end;
    Integer item;
    Integer booker;
    Integer status;
}

