package com.epam.meetingroombooking.repository;

import com.epam.meetingroombooking.model.Booking;
import com.epam.meetingroombooking.model.BookingStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingStatusHistoryRepository extends JpaRepository<BookingStatusHistory, Long> {
    List<BookingStatusHistory> findByBookingOrderByUpdatedAtDesc(Booking booking);
}
