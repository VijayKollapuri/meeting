package com.epam.meetingroombooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RoomDto {
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Min(1)
    private Integer capacity;

    @NotNull
    private Integer floorNumber;

    public RoomDto() {}

    public RoomDto(Long id, String name, Integer capacity, Integer floorNumber) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.floorNumber = floorNumber;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public Integer getFloorNumber() { return floorNumber; }
    public void setFloorNumber(Integer floorNumber) { this.floorNumber = floorNumber; }
}
