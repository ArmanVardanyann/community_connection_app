package com.community.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class BookingDto {

    @JsonProperty("id")
    private Long id;

    @NotBlank(message = "User is required")
    @JsonProperty("user")
    private UserDto user;

    @NotBlank(message = "Parking slot is required")
    @JsonProperty("parkingSlot")
    private ParkingSlotDto parkingSlot;

    @NotBlank(message = "Start time is required")
    @JsonProperty("startTime")
    private LocalDateTime startTime;

    @NotBlank(message = "End time is required")
    @JsonProperty("endTime")
    private LocalDateTime endTime;

    @JsonProperty("comment")
    private String comment;

}
