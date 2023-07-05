package com.community.app.service;

import com.community.app.dto.ParkingSlotDto;
import com.community.app.entity.ParkingSlot;
import com.community.app.exception.NotFoundException;
import com.community.app.mapper.ParkingSlotMapper;
import com.community.app.repository.ParkingSlotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.community.app.service.UserServiceTest.NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingSlotServiceTest {
    @Mock
    private ParkingSlotRepository parkingSlotRepository;

    @Mock
    private ParkingSlotMapper parkingSlotMapper;

    @InjectMocks
    private ParkingSlotService parkingSlotService;

    @Test
    public void testGetParkingSlotById() {
        // Given
        Long parkingSlotId = 1L;
        ParkingSlot parkingSlot = new ParkingSlot();
        parkingSlot.setId(parkingSlotId);
        ParkingSlotDto expectedDto = new ParkingSlotDto();
        expectedDto.setId(parkingSlotId);

        when(parkingSlotRepository.findById(parkingSlotId)).thenReturn(Optional.of(parkingSlot));
        when(parkingSlotMapper.parkingSlotToParkingSlotDto(parkingSlot)).thenReturn(expectedDto);

        // When
        ParkingSlotDto result = parkingSlotService.getParkingSlotById(parkingSlotId);

        // Then
        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
    }

    @Test
    public void testGetParkingSlotById_ThrowsNotFoundException() {
        // Given
        Long parkingSlotId = 1L;
        when(parkingSlotRepository.findById(parkingSlotId)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(NotFoundException.class, () ->
                parkingSlotService.getParkingSlotById(parkingSlotId));
        String actualMessage = exception.getMessage();

        // Then
        assertEquals(NOT_FOUND, actualMessage);
    }

    @Test
    public void testGetAllParkingSlots() {
        // Given
        List<ParkingSlot> parkingSlots = Arrays.asList(new ParkingSlot(), new ParkingSlot());
        when(parkingSlotRepository.findAll()).thenReturn(parkingSlots);

        // When
        List<ParkingSlotDto> result = parkingSlotService.getAllParkingSlots();

        // Then
        assertEquals(parkingSlots.size(), result.size());
    }

    @Test
    public void testCreateParkingSlot() {
        // Given
        ParkingSlotDto parkingSlotDto = new ParkingSlotDto();
        ParkingSlot parkingSlot = new ParkingSlot();

        when(parkingSlotMapper.parkingSlotDtoToParkingSlot(parkingSlotDto)).thenReturn(parkingSlot);
        when(parkingSlotRepository.save(parkingSlot)).thenReturn(parkingSlot);
        when(parkingSlotMapper.parkingSlotToParkingSlotDto(parkingSlot)).thenReturn(parkingSlotDto);

        // When
        ParkingSlotDto result = parkingSlotService.createParkingSlot(parkingSlotDto);

        // Then
        assertNotNull(result);
        assertEquals(parkingSlotDto, result);
    }

    @Test
    public void testDeleteParkingSlot() {
        // Given
        Long parkingSlotId = 1L;
        when(parkingSlotRepository.existsById(parkingSlotId)).thenReturn(true);

        // When
        parkingSlotService.deleteParkingSlot(parkingSlotId);

        // Then
        verify(parkingSlotRepository, times(1)).deleteById(parkingSlotId);
    }

    @Test
    public void testDeleteParkingSlot_ThrowsNotFoundException() {
        // Given
        Long parkingSlotId = 1L;
        when(parkingSlotRepository.existsById(parkingSlotId)).thenReturn(false);

        // When
        Exception exception = assertThrows(NotFoundException.class, () ->
                parkingSlotService.deleteParkingSlot(parkingSlotId));
        String actualMessage = exception.getMessage();

        // Then
        assertEquals(NOT_FOUND, actualMessage);
    }

    @Test
    public void testUpdateParkingSlot() {
        // Given
        Long parkingSlotId = 1L;
        ParkingSlotDto parkingSlotDto = new ParkingSlotDto();
        ParkingSlot updatedSlot = new ParkingSlot();

        when(parkingSlotRepository.findById(parkingSlotId)).thenReturn(Optional.of(updatedSlot));
        when(parkingSlotRepository.save(updatedSlot)).thenReturn(updatedSlot);
        when(parkingSlotMapper.parkingSlotToParkingSlotDto(updatedSlot)).thenReturn(parkingSlotDto);

        // When
        ParkingSlotDto result = parkingSlotService.updateParkingSlot(parkingSlotId, parkingSlotDto);

        // Then
        assertNotNull(result);
        assertEquals(parkingSlotDto, result);
    }

    @Test
    public void testUpdateParkingSlot_ThrowsNotFoundException() {
        // Given
        Long parkingSlotId = 1L;
        ParkingSlotDto parkingSlotDto = new ParkingSlotDto();

        // When
        Exception exception = assertThrows(NotFoundException.class, () ->
                parkingSlotService.updateParkingSlot(parkingSlotId, parkingSlotDto));
        String actualMessage = exception.getMessage();

        // Then
        assertEquals(NOT_FOUND, actualMessage);
    }
}
