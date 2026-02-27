package com.epam.meetingroombooking.controller;

import com.epam.meetingroombooking.model.Booking;
import com.epam.meetingroombooking.model.BookingStatus;
import com.epam.meetingroombooking.model.Room;
import com.epam.meetingroombooking.model.User;
import com.epam.meetingroombooking.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    void getPendingBookings_ShouldReturnList() throws Exception {
        Booking booking = createSampleBooking();
        when(adminService.getAllPendingBookings()).thenReturn(Arrays.asList(booking));

        mockMvc.perform(get("/api/admin/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].agenda").value("Test Agenda"))
                .andExpect(jsonPath("$[0].roomName").value("Conference Room 1"));
    }

    @Test
    void approveBooking_ShouldReturnUpdatedBooking() throws Exception {
        Booking booking = createSampleBooking();
        booking.setStatus(BookingStatus.APPROVED);
        when(adminService.approveBooking(anyLong())).thenReturn(booking);

        mockMvc.perform(post("/api/admin/approve/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void rejectBooking_ShouldReturnUpdatedBooking() throws Exception {
        Booking booking = createSampleBooking();
        booking.setStatus(BookingStatus.REJECTED);
        when(adminService.rejectBooking(anyLong())).thenReturn(booking);

        mockMvc.perform(post("/api/admin/reject/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"));
    }

    private Booking createSampleBooking() {
        User user = new User();
        user.setUsername("testuser");

        Room room = new Room();
        room.setName("Conference Room 1");
        room.setFloorNumber(1);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setUser(user);
        booking.setRoom(room);
        booking.setStartTime(LocalDateTime.now().plusHours(1));
        booking.setEndTime(LocalDateTime.now().plusHours(2));
        booking.setAgenda("Test Agenda");
        booking.setStatus(BookingStatus.PENDING);
        return booking;
    }
}
