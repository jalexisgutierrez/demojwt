package com.company.parking.application.service;

import com.company.parking.domain.model.ParkingLot;
import com.company.parking.domain.model.VehicleEntry;
import com.company.parking.domain.model.VehicleHistory;
import com.company.parking.domain.port.EntryPort;
import com.company.parking.domain.port.ParkingLotPort;
import com.company.parking.util.PlateValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EntryPortUseCase {

    private final EntryPort entryPort;
    private final ParkingLotPort lotPort;

    public VehicleEntry enter(UUID lotId, String plate, UUID ownerUserId) {
        if (!PlateValidator.isValid(plate)) throw new IllegalArgumentException("Placa inválida (6 alfanuméricos, sin ñ)");
        ParkingLot lot = lotPort.findById(lotId)
                .orElseThrow(() -> new IllegalArgumentException("Parking lot not found"));
        if (!lot.getOwnerUserId().equals(ownerUserId))
            throw new IllegalArgumentException("No puedes operar sobre parqueaderos de otro usuario");

        long inside = entryPort.countActiveByLot(lotId);
        if (inside >= lot.getCapacity()) throw new IllegalArgumentException("Capacidad completa");

        entryPort.findActiveByPlate(plate.toUpperCase()).ifPresent(e -> {
            throw new IllegalArgumentException("La placa ya está dentro de otro parqueadero");
        });

        var entry = VehicleEntry.builder()
                .plate(plate.toUpperCase())
                .parkingLot(lot)
                .enteredAt(Instant.now())
                .build();
        return entryPort.saveEntry(entry);
    }

    public VehicleHistory exit(UUID lotId, String plate, UUID ownerUserId) {
        ParkingLot lot = lotPort.findById(lotId)
                .orElseThrow(() -> new IllegalArgumentException("Parking lot not found"));
        if (!lot.getOwnerUserId().equals(ownerUserId))
            throw new IllegalArgumentException("No puedes operar sobre parqueaderos de otro usuario");

        var entry = entryPort.findActiveByPlate(plate.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("La placa no está dentro"));

        if (!entry.getParkingLot().getId().equals(lotId))
            throw new IllegalArgumentException("La placa está en otro parqueadero");

        Instant now = Instant.now();
        long minutes = Duration.between(entry.getEnteredAt(), now).toMinutes();
        long hours = Math.max(1, (long) Math.ceil(minutes / 60.0));
        BigDecimal cost = lot.getHourlyRate().multiply(BigDecimal.valueOf(hours)).setScale(2, RoundingMode.HALF_UP);

        var hist = VehicleHistory.builder()
                .plate(entry.getPlate())
                .parkingLot(lot)
                .enteredAt(entry.getEnteredAt())
                .exitedAt(now)
                .totalCost(cost)
                .build();

        entryPort.deleteEntry(entry);
        return entryPort.saveHistory(hist);
    }

    public List<VehicleEntry> searchParkedLike(UUID lotId, String like, UUID ownerUserId) {
        ParkingLot lot = lotPort.findById(lotId)
                .orElseThrow(() -> new IllegalArgumentException("Parking lot not found"));
        if (!lot.getOwnerUserId().equals(ownerUserId))
            throw new IllegalArgumentException("No puedes consultar parqueaderos de otro usuario");

        return entryPort.searchActiveByLotAndPlateLike(lot, like);
    }
}
