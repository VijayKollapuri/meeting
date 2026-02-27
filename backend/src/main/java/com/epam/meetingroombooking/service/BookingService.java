package com.epam.meetingroombooking.service;

import com.epam.meetingroombooking.exception.BookingConflictException;
import com.epam.meetingroombooking.exception.InvalidBookingStateException;
import com.epam.meetingroombooking.exception.RoomNotFoundException;
import com.epam.meetingroombooking.exception.UnauthorizedException;
import com.epam.meetingroombooking.model.Booking;
import com.epam.meetingroombooking.model.BookingStatus;
import com.epam.meetingroombooking.model.Room;
import com.epam.meetingroombooking.model.User;
import com.epam.meetingroombooking.model.BookingStatusHistory;
import com.epam.meetingroombooking.repository.BookingRepository;
import com.epam.meetingroombooking.repository.BookingStatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingStatusHistoryRepository historyRepository;

    @Autowired
    private RoomService roomService;

    public List<Booking> getMyBookings(User user) {
        return bookingRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Booking> getMyBookingsFiltered(User user, BookingStatus status) {
        return bookingRepository.findByUserAndStatusOrderByCreatedAtDesc(user, status);
    }

    @Transactional
    public Booking createBooking(Booking booking, User user) {
        validateBooking(booking);
        booking.setUser(user);
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());
        Booking savedBooking = bookingRepository.save(booking);
        saveHistory(savedBooking, "Initial booking request");
        return savedBooking;
    }

    public Booking updateBooking(Long id, Booking bookingDetails, User user) {
        Booking booking = getBookingById(id);
        
        if (!booking.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("Unauthorized to update this booking");
        }
        
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new InvalidBookingStateException("Cannot update booking once it is not pending");
        }

        validateBooking(bookingDetails);
        
        booking.setRoom(bookingDetails.getRoom());
        booking.setStartTime(bookingDetails.getStartTime());
        booking.setEndTime(bookingDetails.getEndTime());
        booking.setAgenda(bookingDetails.getAgenda());
        
        return bookingRepository.save(booking);
    }

    @Transactional
    public void cancelBooking(Long id, User user) {
        Booking booking = getBookingById(id);
        
        if (!booking.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("Unauthorized to cancel this booking");
        }

        if (booking.getStatus() == BookingStatus.APPROVED || booking.getStatus() == BookingStatus.PENDING) {
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);
            saveHistory(booking, "Cancelled by user");
        } else {
            throw new InvalidBookingStateException("Cannot cancel booking in status: " + booking.getStatus());
        }
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException("Booking not found with id: " + id));
    }

    private void validateBooking(Booking booking) {
        // Minimum 10 minutes duration
        Duration duration = Duration.between(booking.getStartTime(), booking.getEndTime());
        if (duration.toMinutes() < 10) {
            throw new BookingConflictException("Minimum booking duration is 10 minutes");
        }

        // Only future date/time
        if (booking.getStartTime().isBefore(LocalDateTime.now())) {
            throw new BookingConflictException("Only future date/time bookings allowed");
        }

        if (booking.getEndTime().isBefore(booking.getStartTime())) {
            throw new BookingConflictException("End time must be after start time");
        }

        // Overlap check for approved bookings
        List<Booking> overlapping = bookingRepository.findOverlappingApprovedBookings(
                booking.getRoom(), booking.getStartTime(), booking.getEndTime());
        
        // If updating, exclude self from overlap check
        if (booking.getId() != null) {
            overlapping.removeIf(b -> b.getId().equals(booking.getId()));
        }

        if (!overlapping.isEmpty()) {
            throw new BookingConflictException("Room is already booked for the selected time slot");
        }
    }

    public List<Booking> getAllPendingBookings() {
        return bookingRepository.findByStatus(BookingStatus.PENDING);
    }

    @Transactional
    public Booking updateBookingStatus(Long id, BookingStatus status) {
        Booking booking = getBookingById(id);
        
        if (status == BookingStatus.APPROVED) {
            // Final check for overlap before approving
            List<Booking> overlapping = bookingRepository.findOverlappingApprovedBookings(
                    booking.getRoom(), booking.getStartTime(), booking.getEndTime());
            if (!overlapping.isEmpty()) {
                throw new BookingConflictException("Cannot approve. Room is now booked by another request.");
            }
        }
        
        booking.setStatus(status);
        Booking updatedBooking = bookingRepository.save(booking);
        saveHistory(updatedBooking, "Status updated by admin to " + status);
        return updatedBooking;
    }

    private void saveHistory(Booking booking, String remarks) {
        BookingStatusHistory history = new BookingStatusHistory(
                booking, booking.getStatus(), LocalDateTime.now(), remarks);
        historyRepository.save(history);
    }

    public List<BookingStatusHistory> getBookingHistory(Long bookingId, User user) {
        Booking booking = getBookingById(bookingId);
        if (!booking.getUser().getId().equals(user.getId()) && !isAdmin(user)) {
             throw new UnauthorizedException("Unauthorized to view history");
        }
        return historyRepository.findByBookingOrderByUpdatedAtDesc(booking);
    }

    private boolean isAdmin(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.name().equals("ADMIN"));
    }
}
