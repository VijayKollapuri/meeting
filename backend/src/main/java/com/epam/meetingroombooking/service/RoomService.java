package com.epam.meetingroombooking.service;

import com.epam.meetingroombooking.exception.BookingConflictException;
import com.epam.meetingroombooking.exception.InvalidBookingStateException;
import com.epam.meetingroombooking.exception.RoomNotFoundException;
import com.epam.meetingroombooking.exception.UnauthorizedException;
import com.epam.meetingroombooking.model.Room;
import com.epam.meetingroombooking.repository.BookingRepository;
import com.epam.meetingroombooking.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException("Room not found with id: " + id));
    }

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    public Room updateRoom(Long id, Room roomDetails) {
        Room room = getRoomById(id);
        room.setName(roomDetails.getName());
        room.setCapacity(roomDetails.getCapacity());
        room.setFloorNumber(roomDetails.getFloorNumber());
        return roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        Room room = getRoomById(id);
        if (bookingRepository.existsByRoom(room)) {
            throw new InvalidBookingStateException("Cannot delete room with existing bookings");
        }
        roomRepository.delete(room);
    }
}
