package com.epam.meetingroombooking.service;

import com.epam.meetingroombooking.exception.RoomNotFoundException;
import com.epam.meetingroombooking.model.Room;
import com.epam.meetingroombooking.repository.BookingRepository;
import com.epam.meetingroombooking.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllRooms_ShouldReturnList() {
        Room room = new Room();
        when(roomRepository.findAll()).thenReturn(Arrays.asList(room));

        List<Room> result = roomService.getAllRooms();

        assertEquals(1, result.size());
        verify(roomRepository, times(1)).findAll();
    }

    @Test
    void getRoomById_Success() {
        Room room = new Room();
        room.setId(1L);
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        Room result = roomService.getRoomById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getRoomById_NotFound() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> roomService.getRoomById(1L));
    }

    @Test
    void createRoom_ShouldSave() {
        Room room = new Room();
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        Room result = roomService.createRoom(room);

        assertNotNull(result);
        verify(roomRepository).save(room);
    }

    @Test
    void deleteRoom_Success() {
        Room room = new Room();
        room.setId(1L);
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(bookingRepository.existsByRoom(room)).thenReturn(false);

        roomService.deleteRoom(1L);

        verify(roomRepository).delete(room);
    }
}
