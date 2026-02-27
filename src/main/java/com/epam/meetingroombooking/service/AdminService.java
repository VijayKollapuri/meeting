package com.epam.meetingroombooking.service;

import com.epam.meetingroombooking.model.Booking;
import com.epam.meetingroombooking.model.BookingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private BookingService bookingService;

    public List<Booking> getAllPendingBookings() {
        return bookingService.getAllPendingBookings();
    }

    public Booking approveBooking(Long bookingId) {
        return bookingService.updateBookingStatus(bookingId, BookingStatus.APPROVED);
    }

    public Booking rejectBooking(Long bookingId) {
        return bookingService.updateBookingStatus(bookingId, BookingStatus.REJECTED);
    }
}
