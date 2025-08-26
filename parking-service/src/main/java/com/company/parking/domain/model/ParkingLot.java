package com.company.parking.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="parking_lot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingLot {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private Integer capacity;

    @Column(nullable=false, name="hourly_rate")
    private BigDecimal hourlyRate;

    @Column(nullable=false, name="owner_user_id")
    private UUID ownerUserId;

    @Column(nullable=false, name="owner_email")
    private String ownerEmail;

    @Column(nullable=false, name="created_at")
    private Instant createdAt;
}
