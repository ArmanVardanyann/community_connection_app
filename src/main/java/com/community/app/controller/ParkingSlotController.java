package com.community.app.controller;

import com.community.app.dto.ParkingSlotDto;
import com.community.app.service.ParkingSlotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/parkings")
@RequiredArgsConstructor
public class ParkingSlotController {

    private final ParkingSlotService parkingSlotService;

    @GetMapping("/{id}")
    public ResponseEntity<ParkingSlotDto> getParkingSlotById(@PathVariable Long id) {
        ParkingSlotDto parkingSlotDto = parkingSlotService.getParkingSlotById(id);
        return ResponseEntity.ok(parkingSlotDto);
    }

    @GetMapping
    public List<ParkingSlotDto> getAllParkingSlots() {
        return parkingSlotService.getAllParkingSlots();
    }

    @PostMapping
    public ResponseEntity<ParkingSlotDto> createParkingSlot(
            @Valid @RequestBody ParkingSlotDto parkingSlotDto) {
        ParkingSlotDto createdSlot = parkingSlotService.createParkingSlot(parkingSlotDto);
        return ResponseEntity.created(URI.create("/api/parkings/" + createdSlot.getId()))
                .body(createdSlot);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ParkingSlotDto> deleteParkingSlot(@PathVariable Long id) {
        parkingSlotService.deleteParkingSlot(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParkingSlotDto> updateParkingSlot(@PathVariable Long id,
                                                            @RequestBody ParkingSlotDto slotDto) {
        ParkingSlotDto updatedSlot = parkingSlotService.updateParkingSlot(id, slotDto);
        return ResponseEntity.ok(updatedSlot);
    }

}
