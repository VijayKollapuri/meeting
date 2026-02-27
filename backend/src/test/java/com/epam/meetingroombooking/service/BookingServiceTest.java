package com.epam.meetingroombooking.service;

import com.epam.meetingroombooking.exception.BookingConflictException;
import com.epam.meetingroombooking.model.Booking;
import com.epam.meetingroombooking.model.BookingStatus;
import com.epam.meetingroombooking.model.Room;
import com.epam.meetingroombooking.model.User;
import com.epam.meetingroombooking.repository.BookingRepository;
import com.epam.meetingroombooking.repository.BookingStatusHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingStatusHistoryRepository historyRepository;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private BookingService bookingService;

    private User user;
    private Room room;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        room = new Room();
        room.setId(1L);
        room.setName("Conference Room 1");
    }

    @Test
    public void testCreateBooking_Success() {
        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setStartTime(LocalDateTime.now().plusHours(1));
        booking.setEndTime(LocalDateTime.now().plusHours(2));
        booking.setAgenda("Discuss project progress");

        when(bookingRepository.findOverlappingApprovedBookings(any(), any(), any())).thenReturn(new ArrayList<>());
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Booking createdBooking = bookingService.createBooking(booking, user);

        assertNotNull(createdBooking);
        assertEquals(user, createdBooking.getUser());
        assertEquals(BookingStatus.PENDING, createdBooking.getStatus());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    public void testCreateBooking_MinimumDurationFail() {
        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setStartTime(LocalDateTime.now().plusHours(1));
        booking.setEndTime(LocalDateTime.now().plusHours(1).plusMinutes(5));
        booking.setAgenda("Quick sync");

        BookingConflictException exception = assertThrows(BookingConflictException.class, () -> {
            bookingService.createBooking(booking, user);
        });

        assertEquals("Minimum booking duration is 10 minutes", exception.getMessage());
    }

    @Test
    public void testCreateBooking_PastDateFail() {
        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setStartTime(LocalDateTime.now().minusHours(1));
        booking.setEndTime(LocalDateTime.now().plusHours(1));
        booking.setAgenda("Historical meeting");

        BookingConflictException exception = assertThrows(BookingConflictException.class, () -> {
            bookingService.createBooking(booking, user);
        });

        assertEquals("Only future date/time bookings allowed", exception.getMessage());
    }

    @Test
    public void testCreateBooking_OverlapFail() {
        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setStartTime(LocalDateTime.now().plusHours(1));
        booking.setEndTime(LocalDateTime.now().plusHours(2));
        booking.setAgenda("Overlapping meeting");

        List<Booking> overlapping = new ArrayList<>();
        overlapping.add(new Booking());

        when(bookingRepository.findOverlappingApprovedBookings(any(), any(), any())).thenReturn(overlapping);

        BookingConflictException exception = assertThrows(BookingConflictException.class, () -> {
            bookingService.createBooking(booking, user);
        });

        assertEquals("Room is already booked for the selected time slot", exception.getMessage());
    }
}