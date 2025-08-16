package com.company.parking.web.dto;

import com.company.parking.domain.model.VehicleEntry;

import java.time.Instant;
import java.util.UUID;

public record VehicleEntryDto(
        UUID id,
        String plate,
        UUID parkingLotId,
        Instant enteredAt
) {
    public static VehicleEntryDto from(VehicleEntry e) {
        var lotId = e.getParkingLot() != null ? e.getParkingLot().getId() : null;
        return new VehicleEntryDto(e.getId(), e.getPlate(), lotId, e.getEnteredAt());
    }
}
