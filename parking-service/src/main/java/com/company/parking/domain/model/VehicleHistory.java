package com.company.parking.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="vehicle_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleHistory {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable=false, length=6)
    private String plate;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="parking_lot_id", nullable=false)
    private ParkingLot parkingLot;

    @Column(nullable=false, name="entered_at")
    private Instant enteredAt;

    @Column(nullable=false, name="exited_at")
    private Instant exitedAt;

    @Column(nullable=false, name="total_cost")
    private BigDecimal totalCost;
}
