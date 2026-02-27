package com.epam.meetingroombooking.controller;

import com.epam.meetingroombooking.dto.BookingRequestDto;
import com.epam.meetingroombooking.dto.BookingResponseDto;
import com.epam.meetingroombooking.model.Booking;
import com.epam.meetingroombooking.model.BookingStatus;
import com.epam.meetingroombooking.model.BookingStatusHistory;
import com.epam.meetingroombooking.model.Room;
import com.epam.meetingroombooking.model.User;
import com.epam.meetingroombooking.service.BookingService;
import com.epam.meetingroombooking.service.RoomService;
import com.epam.meetingroombooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @GetMapping("/my")
    public List<BookingResponseDto> getMyBookings(Authentication authentication, 
                                                @RequestParam(required = false) BookingStatus status) {
        User user = userService.findByUsername(authentication.getName()).orElseThrow();
        List<Booking> bookings;
        if (status != null) {
            bookings = bookingService.getMyBookingsFiltered(user, status);
        } else {
            bookings = bookingService.getMyBookings(user);
        }
        return bookings.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(Authentication authentication, 
                                                          @Valid @RequestBody BookingRequestDto request) {
        User user = userService.findByUsername(authentication.getName()).orElseThrow();
        Room room = roomService.getRoomById(request.getRoomId());
        
        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setAgenda(request.getAgenda());
        
        return ResponseEntity.ok(convertToDto(bookingService.createBooking(booking, user)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResponseDto> updateBooking(Authentication authentication,
                                                          @PathVariable Long id,
                                                          @Valid @RequestBody BookingRequestDto request) {
        User user = userService.findByUsername(authentication.getName()).orElseThrow();
        Room room = roomService.getRoomById(request.getRoomId());
        
        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setAgenda(request.getAgenda());
        
        return ResponseEntity.ok(convertToDto(bookingService.updateBooking(id, booking, user)));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(Authentication authentication, @PathVariable Long id) {
        User user = userService.findByUsername(authentication.getName()).orElseThrow();
        bookingService.cancelBooking(id, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<BookingStatusHistory>> getBookingHistory(Authentication authentication, @PathVariable Long id) {
        User user = userService.findByUsername(authentication.getName()).orElseThrow();
        return ResponseEntity.ok(bookingService.getBookingHistory(id, user));
    }

    private BookingResponseDto convertToDto(Booking booking) {
        Duration duration = Duration.between(booking.getStartTime(), booking.getEndTime());
        long minutes = duration.toMinutes();
        String durationStr = minutes < 60 ? minutes + " mins" : (minutes / 60) + "h " + (minutes % 60) + "m";
        
        return new BookingResponseDto(
                booking.getId(),
                booking.getRoom().getName(),
                booking.getRoom().getFloorNumber(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getAgenda(),
                booking.getStatus(),
                booking.getUser().getUsername(),
                durationStr
        );
    }
}
