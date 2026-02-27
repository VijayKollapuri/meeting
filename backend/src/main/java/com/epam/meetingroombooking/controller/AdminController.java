package com.epam.meetingroombooking.controller;

import com.epam.meetingroombooking.dto.BookingResponseDto;
import com.epam.meetingroombooking.model.Booking;
import com.epam.meetingroombooking.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/pending")
    public List<BookingResponseDto> getPendingBookings() {
        return adminService.getAllPendingBookings().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<BookingResponseDto> approveBooking(@PathVariable Long id) {
        return ResponseEntity.ok(convertToDto(adminService.approveBooking(id)));
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<BookingResponseDto> rejectBooking(@PathVariable Long id) {
        return ResponseEntity.ok(convertToDto(adminService.rejectBooking(id)));
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
