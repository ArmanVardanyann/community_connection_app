package com.community.app.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryUtility {
    public static final String PARKING_STATUS = """
            (SELECT IF((SELECT count(b.id) FROM bookings b
                      WHERE b.parking_slot_id = id
                          AND CURRENT_TIMESTAMP BETWEEN b.start_time AND b.end_time) > 0 ,
                'BOOKED',
                'FREE'))
            """;
}
