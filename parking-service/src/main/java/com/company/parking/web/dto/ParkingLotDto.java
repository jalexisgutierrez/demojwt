package com.company.parking.web.dto;

import com.company.parking.domain.model.ParkingLot;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ParkingLotDto(
        UUID id,
        String name,
        int capacity,
        BigDecimal hourlyRate,
        UUID ownerUserId,
        String ownerEmail,
        Instant createdAt
) {
    public static ParkingLotDto from(ParkingLot p) {
        return new ParkingLotDto(
                p.getId(), p.getName(), p.getCapacity(), p.getHourlyRate(),
                p.getOwnerUserId(), p.getOwnerEmail(), p.getCreatedAt()
        );
    }
}
