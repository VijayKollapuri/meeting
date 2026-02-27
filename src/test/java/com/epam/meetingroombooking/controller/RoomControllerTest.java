package com.epam.meetingroombooking.controller;

import com.epam.meetingroombooking.dto.RoomDto;
import com.epam.meetingroombooking.model.Room;
import com.epam.meetingroombooking.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RoomControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomController roomController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(roomController).build();
    }

    @Test
    void getAllRooms_ShouldReturnList() throws Exception {
        Room room = new Room();
        room.setId(1L);
        room.setName("Conference Room 1");
        room.setCapacity(10);
        room.setFloorNumber(1);

        when(roomService.getAllRooms()).thenReturn(Arrays.asList(room));

        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Conference Room 1"));
    }

    @Test
    void getRoomById_ShouldReturnRoom() throws Exception {
        Room room = new Room();
        room.setId(1L);
        room.setName("Conference Room 1");

        when(roomService.getRoomById(anyLong())).thenReturn(room);

        mockMvc.perform(get("/api/rooms/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Conference Room 1"));
    }

    @Test
    void createRoom_ShouldReturnCreatedRoom() throws Exception {
        RoomDto roomDto = new RoomDto(null, "New Room", 5, 2);
        Room room = new Room();
        room.setId(1L);
        room.setName("New Room");
        room.setCapacity(5);
        room.setFloorNumber(2);

        when(roomService.createRoom(any(Room.class))).thenReturn(room);

        mockMvc.perform(post("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roomDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Room"));
    }
}
