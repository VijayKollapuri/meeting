package com.epam.meetingroombooking.repository;

import com.epam.meetingroombooking.model.Booking;
import com.epam.meetingroombooking.model.BookingStatus;
import com.epam.meetingroombooking.model.Room;
import com.epam.meetingroombooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByUserOrderByCreatedAtDesc(User user);
    
    List<Booking> findByUserAndStatusOrderByCreatedAtDesc(User user, BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.room = :room AND b.status = 'APPROVED' AND " +
           "((b.startTime < :endTime AND b.endTime > :startTime))")
    List<Booking> findOverlappingApprovedBookings(@Param("room") Room room, 
                                                  @Param("startTime") LocalDateTime startTime, 
                                                  @Param("endTime") LocalDateTime endTime);

    List<Booking> findByStatus(BookingStatus status);

    boolean existsByRoom(Room room);
}
