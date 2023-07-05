package com.community.app.entity;

import com.community.app.enumeration.ParkingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;

import java.util.ArrayList;
import java.util.List;

import static com.community.app.utility.QueryUtility.PARKING_STATUS;

@Entity
@Table(name = "parking_slots")
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ParkingSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slot_number", unique = true)
    private String slotNumber;

    @Formula(PARKING_STATUS)
    @Enumerated(EnumType.STRING)
    private ParkingStatus parkingStatus;

    @OneToMany(mappedBy = "parkingSlot")
    private List<Booking> bookingList = new ArrayList<>();

}
