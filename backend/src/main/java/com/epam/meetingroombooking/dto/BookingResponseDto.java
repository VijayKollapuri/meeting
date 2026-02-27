package com.epam.meetingroombooking.dto;

import com.epam.meetingroombooking.model.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class BookingResponseDto {
    private Long id;
    private String roomName;
    private Integer floorNumber;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String agenda;
    private BookingStatus status;
    private String username;
    private String duration; // e.g. "45 minutes" or "1 hour 20 minutes"

    public BookingResponseDto() {}

    public BookingResponseDto(Long id, String roomName, Integer floorNumber, LocalDateTime startTime, LocalDateTime endTime, String agenda, BookingStatus status, String username, String duration) {
        this.id = id;
        this.roomName = roomName;
        this.floorNumber = floorNumber;
        this.startTime = startTime;
        this.endTime = endTime;
        this.agenda = agenda;
        this.status = status;
        this.username = username;
        this.duration = duration;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }
    public Integer getFloorNumber() { return floorNumber; }
    public void setFloorNumber(Integer floorNumber) { this.floorNumber = floorNumber; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public String getAgenda() { return agenda; }
    public void setAgenda(String agenda) { this.agenda = agenda; }
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
}
