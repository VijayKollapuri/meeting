package com.epam.meetingroombooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class BookingRequestDto {
    @NotNull
    private Long roomId;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    private String agenda;

    public BookingRequestDto() {}

    public BookingRequestDto(Long roomId, LocalDateTime startTime, LocalDateTime endTime, String agenda) {
        this.roomId = roomId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.agenda = agenda;
    }

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public String getAgenda() { return agenda; }
    public void setAgenda(String agenda) { this.agenda = agenda; }
}
