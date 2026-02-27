package com.epam.meetingroombooking.service;

import com.epam.meetingroombooking.model.Booking;
import com.epam.meetingroombooking.model.BookingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminServiceTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPendingBookings_ShouldReturnList() {
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.PENDING);
        when(bookingService.getAllPendingBookings()).thenReturn(Arrays.asList(booking));

        List<Booking> result = adminService.getAllPendingBookings();

        assertEquals(1, result.size());
        assertEquals(BookingStatus.PENDING, result.get(0).getStatus());
    }

    @Test
    void approveBooking_ShouldCallUpdateStatus() {
        adminService.approveBooking(1L);
        verify(bookingService).updateBookingStatus(1L, BookingStatus.APPROVED);
    }

    @Test
    void rejectBooking_ShouldCallUpdateStatus() {
        adminService.rejectBooking(1L);
        verify(bookingService).updateBookingStatus(1L, BookingStatus.REJECTED);
    }
}
