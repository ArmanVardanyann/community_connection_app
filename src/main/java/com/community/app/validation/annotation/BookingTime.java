package com.community.app.validation.annotation;

import com.community.app.validation.BookingTimeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BookingTimeValidator.class)
public @interface BookingTime {

    /**
     * Error message in case of violating validation constraint.
     *
     * @return error message
     */
    String message() default "Start time should be less than end time";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
