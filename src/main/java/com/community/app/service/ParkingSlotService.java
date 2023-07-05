package com.community.app.service;

import com.community.app.dto.ParkingSlotDto;
import com.community.app.entity.ParkingSlot;
import com.community.app.exception.NotFoundException;
import com.community.app.mapper.ParkingSlotMapper;
import com.community.app.repository.ParkingSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingSlotService {

    private final ParkingSlotRepository parkingSlotRepository;

    private final ParkingSlotMapper parkingSlotMapper;

    public ParkingSlotDto getParkingSlotById(Long id) {
        ParkingSlot parkingSlot = parkingSlotRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        return mapSlotToDto(parkingSlot);
    }

    public List<ParkingSlotDto> getAllParkingSlots() {
        List<ParkingSlot> parkingSlots = parkingSlotRepository.findAll();
        return parkingSlots.stream().map(this::mapSlotToDto).toList();
    }

    public ParkingSlotDto createParkingSlot(ParkingSlotDto parkingSlotDto) {
        ParkingSlot slot = mapDtoToSlot(parkingSlotDto);
        parkingSlotRepository.save(slot);
        return mapSlotToDto(slot);
    }

    public void deleteParkingSlot(Long id) {
        if (!parkingSlotRepository.existsById(id)) {
            throw new NotFoundException();
        }
        parkingSlotRepository.deleteById(id);
    }

    public ParkingSlotDto updateParkingSlot(Long id, ParkingSlotDto slotDto) {
        ParkingSlot slot = parkingSlotRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        updateExistingParkingSlot(slotDto, slot);
        parkingSlotRepository.save(slot);
        return mapSlotToDto(slot);
    }

    private ParkingSlotDto mapSlotToDto(ParkingSlot parkingSlot) {
        return parkingSlotMapper.parkingSlotToParkingSlotDto(parkingSlot);
    }

    private ParkingSlot mapDtoToSlot(ParkingSlotDto parkingSlotDto) {
        return parkingSlotMapper.parkingSlotDtoToParkingSlot(parkingSlotDto);
    }

    private void updateExistingParkingSlot(ParkingSlotDto parkingSlotDto, ParkingSlot parkingSlot) {
        parkingSlotMapper.updateParkingSlot(parkingSlotDto, parkingSlot);
    }
}
