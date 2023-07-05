package com.community.app.dto;

import com.community.app.enumeration.ParkingStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@RequiredArgsConstructor
public class ParkingSlotDto {

    @JsonProperty("id")
    private Long id;

    @NotBlank(message = "Parking slot number is required")
    @JsonProperty("slotNumber")
    private String slotNumber;

    @JsonProperty("parkingStatus")
    private ParkingStatus parkingStatus;

}