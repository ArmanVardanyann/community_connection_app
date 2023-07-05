package com.community.app.service;

import com.community.app.dto.BookingDto;
import com.community.app.dto.ParkingSlotDto;
import com.community.app.dto.UserDto;
import com.community.app.entity.Booking;
import com.community.app.entity.ParkingSlot;
import com.community.app.entity.User;
import com.community.app.exception.AlreadyBookedException;
import com.community.app.exception.NotFoundException;
import com.community.app.mapper.BookingMapper;
import com.community.app.mapper.ParkingSlotMapper;
import com.community.app.mapper.UserMapper;
import com.community.app.repository.BookingRepository;
import com.community.app.repository.ParkingSlotRepository;
import com.community.app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.community.app.service.UserServiceTest.NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ParkingSlotRepository parkingSlotRepository;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ParkingSlotMapper parkingSlotMapper;

    @InjectMocks
    private BookingService bookingService;

    public static final String ALREADY_BOOKED = "slotAlreadyBooked";

    @Test
    public void testCreateBooking() {
        // Given
        BookingDto bookingDto = new BookingDto();
        UserDto userDto = new UserDto();
        ParkingSlotDto parkingSlotDto = new ParkingSlotDto();
        parkingSlotDto.setId(1L);
        User user = new User();
        ParkingSlot parkingSlot = new ParkingSlot();
        parkingSlot.setId(1L);
        Booking booking = new Booking();
        booking.setParkingSlot(parkingSlot);
        booking.setUser(user);
        bookingDto.setUser(userDto);
        bookingDto.setParkingSlot(parkingSlotDto);

        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));
        when(parkingSlotRepository.findById(parkingSlotDto.getId())).thenReturn(Optional.of(parkingSlot));
        when(bookingMapper.bookingDtoToBooking(bookingDto)).thenReturn(booking);
        when(bookingMapper.bookingToBookingDto(booking)).thenReturn(bookingDto);

        // When
        BookingDto result = bookingService.createBooking(bookingDto);

        // Then
        assertNotNull(result);
        assertEquals(bookingDto, result);
    }

    @Test
    public void testCreateBookingInvalidUser_ThrowsNotFoundException() {
        // Given
        BookingDto bookingDto = new BookingDto();
        UserDto userDto = new UserDto();
        bookingDto.setUser(userDto);

        when(userRepository.findById(userDto.getId())).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(NotFoundException.class, () ->
                bookingService.createBooking(bookingDto));
        String actualMessage = exception.getMessage();

        // Then
        assertEquals(NOT_FOUND, actualMessage);
    }

    @Test
    public void testCreateBookingInvalidParkingSlot_ThrowsNotFoundException() {
        // Given
        BookingDto bookingDto = new BookingDto();
        ParkingSlotDto parkingSlotDto = new ParkingSlotDto();
        UserDto userDto = new UserDto();
        User user = new User();
        bookingDto.setParkingSlot(parkingSlotDto);
        bookingDto.setUser(userDto);
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));
        when(parkingSlotRepository.findById(parkingSlotDto.getId())).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(NotFoundException.class, () ->
                bookingService.createBooking(bookingDto));
        String actualMessage = exception.getMessage();

        // Then
        assertEquals(NOT_FOUND, actualMessage);
    }

    @Test
    public void testCreateBookingOverlapped_ThrowsAlreadyBookedException() {
        // Given
        BookingDto bookingDto = new BookingDto();
        Booking booking = new Booking();
        UserDto userDto = new UserDto();
        User user = new User();
        ParkingSlotDto parkingSlotDto = new ParkingSlotDto();
        parkingSlotDto.setSlotNumber("P1");
        ParkingSlot parkingSlot = new ParkingSlot();
        parkingSlot.setSlotNumber("P1");

        booking.setUser(user);
        booking.setParkingSlot(parkingSlot);
        booking.setStartTime(LocalDateTime.of(2023, 7, 4, 11, 0));
        booking.setEndTime(LocalDateTime.of(2023, 7, 4, 11, 30));

        Booking existingBooking = new Booking();
        ParkingSlot existingParkingSlot = new ParkingSlot();
        existingParkingSlot.setSlotNumber("P1");
        existingBooking.setStartTime(LocalDateTime.of(2023, 7, 4, 10, 0));
        existingBooking.setEndTime(LocalDateTime.of(2023, 7, 4, 12, 0));
        User existingUser = new User();
        existingBooking.setUser(existingUser);
        existingBooking.setParkingSlot(existingParkingSlot);

        when(parkingSlotMapper.parkingSlotDtoToParkingSlot(parkingSlotDto))
                .thenReturn(parkingSlot);
        when(userMapper.userDtoToUser(userDto)).thenReturn(user);
        when(bookingMapper.bookingDtoToBooking(bookingDto)).thenReturn(booking);

        user = userMapper.userDtoToUser(userDto);
        parkingSlot = parkingSlotMapper.parkingSlotDtoToParkingSlot(parkingSlotDto);

        bookingDto.setUser(userDto);
        bookingDto.setParkingSlot(parkingSlotDto);
        booking = bookingMapper.bookingDtoToBooking(bookingDto);

        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));
        when(parkingSlotRepository.findById(parkingSlotDto.getId())).thenReturn(Optional.of(parkingSlot));
        when(bookingRepository.findAll()).thenReturn(List.of(booking, existingBooking));

        // When
        Exception exception = assertThrows(AlreadyBookedException.class, () ->
                bookingService.createBooking(bookingDto));
        String actualMessage = exception.getMessage();

        // Then
        assertEquals(ALREADY_BOOKED, actualMessage);
    }

    @Test
    public void testGetBookingById() {
        // Given
        Long bookingId = 1L;
        Booking booking = new Booking();
        booking.setId(bookingId);
        BookingDto expectedDto = new BookingDto();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingMapper.bookingToBookingDto(booking)).thenReturn(expectedDto);

        // When
        BookingDto result = bookingService.getBookingById(bookingId);

        // Then
        assertNotNull(result);
        assertEquals(expectedDto, result);
    }

    @Test
    public void testGetBookingByNotExistingId_ThrowsNotFoundException() {
        // Given
        Long bookingId = 1L;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(NotFoundException.class, () ->
                bookingService.getBookingById(bookingId));
        String actualMessage = exception.getMessage();

        // Then
        assertEquals(NOT_FOUND, actualMessage);
    }

    @Test
    public void testGetAllBookings() {
        // Given
        List<Booking> bookings = Arrays.asList(new Booking(), new Booking());
        when(bookingRepository.findAll()).thenReturn(bookings);

        // When
        List<BookingDto> result = bookingService.getAllBookings();

        // Then
        assertEquals(bookings.size(), result.size());
    }

    @Test
    public void testUpdateBooking() {
        // Given
        Long bookingId = 1L;
        BookingDto bookingDto = new BookingDto();
        Booking existingBooking = new Booking();
        User user = new User();
        UserDto userDto = new UserDto();
        ParkingSlot parkingSlot = new ParkingSlot();
        parkingSlot.setId(1L);
        ParkingSlotDto parkingSlotDto = new ParkingSlotDto();
        parkingSlotDto.setId(1L);
        existingBooking.setId(bookingId);
        existingBooking.setParkingSlot(parkingSlot);
        existingBooking.setUser(user);
        bookingDto.setUser(userDto);
        bookingDto.setParkingSlot(parkingSlotDto);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(existingBooking));
        when(bookingMapper.bookingToBookingDto(existingBooking)).thenReturn(bookingDto);
        when(bookingRepository.save(existingBooking)).thenReturn(existingBooking);
        when(parkingSlotRepository.findById(parkingSlot.getId()))
                .thenReturn(Optional.of(parkingSlot));
        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        // When
        BookingDto result = bookingService.updateBooking(bookingId, bookingDto);

        // Then
        assertNotNull(result);
        assertEquals(bookingDto, result);
        // Additional assertions for updated fields
        assertEquals(bookingDto.getStartTime(), existingBooking.getStartTime());
        assertEquals(bookingDto.getEndTime(), existingBooking.getEndTime());
        assertEquals(bookingDto.getComment(), existingBooking.getComment());
    }

    @Test
    public void testUpdateBookingByNonExistingId_ThrowsNotFoundException() {
        // Given
        Long bookingId = 1L;
        UserDto userDto = new UserDto();
        ParkingSlotDto parkingSlotDto = new ParkingSlotDto();
        BookingDto bookingDto = new BookingDto();
        bookingDto.setUser(userDto);
        bookingDto.setParkingSlot(parkingSlotDto);

        // When
        Exception exception = assertThrows(NotFoundException.class, () ->
                bookingService.updateBooking(bookingId, bookingDto));
        String actualMessage = exception.getMessage();

        // Then
        assertEquals(NOT_FOUND, actualMessage);
    }

    @Test
    public void testDeleteBooking() {
        // Given
        Long bookingId = 1L;

        when(bookingRepository.existsById(bookingId)).thenReturn(true);

        // When
        bookingService.deleteBooking(bookingId);

        // Then
        // No exceptions should be thrown
        verify(bookingRepository, times(1)).deleteById(bookingId);
    }

    @Test
    public void testDeleteBookingByNonExistingId_ThrowsNotFoundException() {
        // Given
        Long bookingId = 1L;

        when(bookingRepository.existsById(bookingId)).thenReturn(false);

        // When
        Exception exception = assertThrows(NotFoundException.class, () ->
                bookingService.deleteBooking(bookingId));
        String actualMessage = exception.getMessage();

        // Then
        assertEquals(NOT_FOUND, actualMessage);
    }

}
