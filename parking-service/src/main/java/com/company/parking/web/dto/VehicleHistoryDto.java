package com.company.parking.web.dto;

import com.company.parking.domain.model.VehicleHistory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record VehicleHistoryDto(
        UUID id,
        String plate,
        UUID parkingLotId,
        Instant enteredAt,
        Instant exitedAt,
        BigDecimal total
) {
    public static VehicleHistoryDto from(VehicleHistory h) {
        var lotId = h.getParkingLot() != null ? h.getParkingLot().getId() : null;
        return new VehicleHistoryDto(h.getId(), h.getPlate(), lotId, h.getEnteredAt(), h.getExitedAt(), h.getTotalCost());
    }
}
