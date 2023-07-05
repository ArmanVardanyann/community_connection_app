package com.community.app.service;

import com.community.app.dto.BookingDto;
import com.community.app.entity.Booking;
import com.community.app.entity.ParkingSlot;
import com.community.app.entity.User;
import com.community.app.exception.AlreadyBookedException;
import com.community.app.exception.NotFoundException;
import com.community.app.mapper.BookingMapper;
import com.community.app.repository.BookingRepository;
import com.community.app.repository.ParkingSlotRepository;
import com.community.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ParkingSlotRepository parkingSlotRepository;
    private final BookingMapper bookingMapper;

    public BookingDto createBooking(BookingDto bookingDto) {
        User user = userRepository.findById(bookingDto.getUser().getId())
                .orElseThrow(NotFoundException::new);
        ParkingSlot parkingSlot = parkingSlotRepository.findById(bookingDto.getParkingSlot().getId())
                .orElseThrow(NotFoundException::new);
        Booking booking = mapDtoToBooking(bookingDto);
        if (!isBookingOverlapped(booking)) {
            booking.setUser(user);
            booking.setParkingSlot(parkingSlot);
            bookingRepository.save(booking);
            return mapBookingToDto(booking);
        }
        throw new AlreadyBookedException();
    }

    public BookingDto getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        return mapBookingToDto(booking);
    }

    public List<BookingDto> getAllBookings() {
        List<Booking> bookingList = bookingRepository.findAll();
        return bookingList.stream().map(this::mapBookingToDto).toList();
    }

    public BookingDto updateBooking(Long id, BookingDto bookingDto) {
        User user = userRepository.findById(bookingDto.getUser().getId())
                .orElseThrow(NotFoundException::new);
        ParkingSlot parkingSlot = parkingSlotRepository.findById(bookingDto.getParkingSlot().getId())
                .orElseThrow(NotFoundException::new);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        updateExistingBooking(bookingDto, booking);
        if (!isBookingOverlapped(booking)) {
            booking.setUser(user);
            booking.setParkingSlot(parkingSlot);
            bookingRepository.save(booking);
            return mapBookingToDto(booking);
        }
        throw new AlreadyBookedException();
    }


    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new NotFoundException();
        }
        bookingRepository.deleteById(id);
    }

    private boolean isBookingOverlapped(Booking newBooking) {
        List<Booking> existingBookings = bookingRepository.findAll();
        ParkingSlot parkingSlot = parkingSlotRepository.findById(
                newBooking.getParkingSlot().getId()).orElseThrow(NotFoundException::new
        );

        return existingBookings.stream()
                .anyMatch(booking ->
                        !booking.equals(newBooking) &&
                                booking.getParkingSlot().getSlotNumber()
                                        .equals(parkingSlot.getSlotNumber()) &&
                                newBooking.getStartTime().isBefore(booking.getEndTime()) &&
                                newBooking.getEndTime().isAfter(booking.getStartTime())
                );
    }

    private BookingDto mapBookingToDto(Booking booking) {
        return bookingMapper.bookingToBookingDto(booking);
    }

    private Booking mapDtoToBooking(BookingDto bookingDto) {
        return bookingMapper.bookingDtoToBooking(bookingDto);
    }

    private void updateExistingBooking(BookingDto bookingDto, Booking booking) {
        bookingMapper.updateBooking(bookingDto, booking);
    }
}
