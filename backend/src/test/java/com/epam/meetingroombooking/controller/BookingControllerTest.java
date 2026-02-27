package com.epam.meetingroombooking.controller;

import com.epam.meetingroombooking.dto.BookingRequestDto;
import com.epam.meetingroombooking.model.Booking;
import com.epam.meetingroombooking.model.BookingStatus;
import com.epam.meetingroombooking.model.Room;
import com.epam.meetingroombooking.model.User;
import com.epam.meetingroombooking.service.BookingService;
import com.epam.meetingroombooking.service.RoomService;
import com.epam.meetingroombooking.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @Mock
    private UserService userService;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private BookingController bookingController;

    private ObjectMapper objectMapper;
    private User testUser;
    private Room testRoom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        testUser = new User();
        testUser.setUsername("testuser");

        testRoom = new Room();
        testRoom.setId(1L);
        testRoom.setName("Conference Room 1");
        testRoom.setFloorNumber(1);
    }

    @Test
    void getMyBookings_ShouldReturnList() throws Exception {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setRoom(testRoom);
        booking.setUser(testUser);
        booking.setStartTime(LocalDateTime.now().plusHours(1));
        booking.setEndTime(LocalDateTime.now().plusHours(2));
        booking.setAgenda("Test");
        booking.setStatus(BookingStatus.PENDING);

        when(bookingService.getMyBookings(any(User.class))).thenReturn(Arrays.asList(booking));

        mockMvc.perform(get("/api/bookings/my").principal(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void createBooking_ShouldReturnCreatedBooking() throws Exception {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(roomService.getRoomById(anyLong())).thenReturn(testRoom);

        BookingRequestDto request = new BookingRequestDto();
        request.setRoomId(1L);
        request.setStartTime(LocalDateTime.now().plusHours(1));
        request.setEndTime(LocalDateTime.now().plusHours(2));
        request.setAgenda("New Meeting");

        Booking savedBooking = new Booking();
        savedBooking.setId(1L);
        savedBooking.setRoom(testRoom);
        savedBooking.setUser(testUser);
        savedBooking.setStartTime(request.getStartTime());
        savedBooking.setEndTime(request.getEndTime());
        savedBooking.setAgenda(request.getAgenda());
        savedBooking.setStatus(BookingStatus.PENDING);

        when(bookingService.createBooking(any(Booking.class), any(User.class))).thenReturn(savedBooking);

        mockMvc.perform(post("/api/bookings")
                .principal(auth)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.agenda").value("New Meeting"));
    }
}
