package com.company.parking.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="vehicle_entry")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleEntry {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable=false, length=6)
    private String plate;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="parking_lot_id", nullable=false)
    private ParkingLot parkingLot;

    @Column(nullable=false, name="entered_at")
    private Instant enteredAt;
}
