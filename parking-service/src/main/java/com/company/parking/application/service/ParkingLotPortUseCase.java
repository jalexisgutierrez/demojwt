package com.company.parking.application.service;

import com.company.parking.domain.model.ParkingLot;
import com.company.parking.domain.port.ParkingLotPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParkingLotPortUseCase {

    private final ParkingLotPort lotPort; // OUT

    public ParkingLot create(String name, int capacity, BigDecimal hourlyRate, UUID ownerUserId, String ownerEmail) {
        if (capacity <= 0) throw new IllegalArgumentException("capacity must be > 0");
        if (hourlyRate == null || hourlyRate.signum() <= 0) throw new IllegalArgumentException("hourlyRate must be > 0");

        var lot = ParkingLot.builder()
                .name(name).capacity(capacity).hourlyRate(hourlyRate)
                .ownerUserId(ownerUserId).ownerEmail(ownerEmail)
                .createdAt(Instant.now()).build();
        return lotPort.save(lot);
    }

    public ParkingLot getOwned(UUID lotId, UUID ownerUserId) {
        return lotPort.findByIdAndOwner(lotId, ownerUserId)
                .orElseThrow(() -> new IllegalArgumentException("Parking lot not found or not owned by user"));
    }

}
