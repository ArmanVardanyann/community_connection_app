package com.community.app.mapper;

import com.community.app.dto.BookingDto;
import com.community.app.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface BookingMapper {
    BookingDto bookingToBookingDto(Booking booking);

    Booking bookingDtoToBooking(BookingDto bookingDto);

    void updateBooking(BookingDto bookingDto,
                       @MappingTarget Booking booking);
}
