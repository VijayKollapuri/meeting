package com.epam.meetingroombooking.controller;

import com.epam.meetingroombooking.dto.RoomDto;
import com.epam.meetingroombooking.model.Room;
import com.epam.meetingroombooking.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping
    public List<RoomDto> getAllRooms() {
        return roomService.getAllRooms().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(convertToDto(roomService.getRoomById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomDto> createRoom(@Valid @RequestBody RoomDto roomDto) {
        Room room = convertToEntity(roomDto);
        return ResponseEntity.ok(convertToDto(roomService.createRoom(room)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomDto> updateRoom(@PathVariable Long id, @Valid @RequestBody RoomDto roomDto) {
        Room room = convertToEntity(roomDto);
        return ResponseEntity.ok(convertToDto(roomService.updateRoom(id, room)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok().build();
    }

    private RoomDto convertToDto(Room room) {
        return new RoomDto(room.getId(), room.getName(), room.getCapacity(), room.getFloorNumber());
    }

    private Room convertToEntity(RoomDto dto) {
        Room room = new Room();
        room.setName(dto.getName());
        room.setCapacity(dto.getCapacity());
        room.setFloorNumber(dto.getFloorNumber());
        return room;
    }
}
