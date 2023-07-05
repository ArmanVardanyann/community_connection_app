package com.community.app.mapper;

import com.community.app.dto.ParkingSlotDto;
import com.community.app.entity.ParkingSlot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface ParkingSlotMapper {

    ParkingSlotDto parkingSlotToParkingSlotDto(ParkingSlot parkingSlot);

    @Mapping(target = "parkingStatus", ignore = true)
    @Mapping(target = "bookingList", ignore = true)
    ParkingSlot parkingSlotDtoToParkingSlot(ParkingSlotDto parkingSlotDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookingList", ignore = true)
    void updateParkingSlot(ParkingSlotDto parkingSlotDto,
                           @MappingTarget ParkingSlot parkingSlot);
}
