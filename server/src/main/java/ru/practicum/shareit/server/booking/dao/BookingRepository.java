package ru.practicum.shareit.server.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.server.booking.model.Booking;
import ru.practicum.shareit.server.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b " +
            "WHERE b.id = :bookingId " +
            "AND (b.booker.id = :userId OR b.item.owner.id = :userId)")
    Optional<Booking> findByIdAndUserIsBookerOrOwner(@Param("bookingId") Long bookingId,
                                                     @Param("userId") Long userId);

    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :userId " +
            "AND b.start <= :now AND b.end >= :now " +
            "ORDER BY b.start DESC")
    List<Booking> findCurrentBookings(@Param("userId") Long userId,
                                      @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :userId " +
            "AND b.end < :now " +
            "ORDER BY b.start DESC")
    List<Booking> findPastBookings(@Param("userId") Long userId,
                                   @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :userId " +
            "AND b.start > :now " +
            "ORDER BY b.start DESC")
    List<Booking> findFutureBookings(@Param("userId") Long userId,
                                     @Param("now") LocalDateTime now);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, Status status);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId " +
            "AND b.start <= :now AND b.end >= :now ORDER BY b.start DESC")
    List<Booking> findCurrentByOwner(@Param("ownerId") Long ownerId,
                                     @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId " +
            "AND b.end < :now ORDER BY b.start DESC")
    List<Booking> findPastByOwner(@Param("ownerId") Long ownerId,
                                  @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId " +
            "AND b.start > :now ORDER BY b.start DESC")
    List<Booking> findFutureByOwner(@Param("ownerId") Long ownerId,
                                    @Param("now") LocalDateTime now);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, Status status);

    Optional<Booking> findFirstByItemIdAndEndBeforeOrderByEndDesc(Long itemId, LocalDateTime now);

    Optional<Booking> findFirstByItemIdAndStartAfterOrderByStartAsc(Long itemId, LocalDateTime now);

    boolean existsByBookerIdAndItemIdAndEndBefore(Long bookerId, Long itemId, LocalDateTime time);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN :itemIds " +
            "AND b.end < :now ORDER BY b.end DESC")
    List<Booking> findLastBookings(@Param("itemIds") List<Long> itemIds,
                                   @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN :itemIds " +
            "AND b.start > :now ORDER BY b.start ASC")
    List<Booking> findNextBookings(@Param("itemIds") List<Long> itemIds,
                                   @Param("now") LocalDateTime now);
}
