package com.community.app.validation;

import com.community.app.dto.BookingDto;
import com.community.app.validation.annotation.BookingTime;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class BookingTimeValidator implements ConstraintValidator<BookingTime, BookingDto> {

    @Override
    public boolean isValid(BookingDto bookingDto, ConstraintValidatorContext context) {
        LocalDateTime startTime = bookingDto.getStartTime();
        LocalDateTime endTime = bookingDto.getEndTime();

        return startTime.isBefore(endTime);

    }
}
