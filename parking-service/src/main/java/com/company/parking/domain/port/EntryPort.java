package com.company.parking.domain.port;

import com.company.parking.domain.model.ParkingLot;
import com.company.parking.domain.model.VehicleEntry;
import com.company.parking.domain.model.VehicleHistory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EntryPort {

    Optional<VehicleEntry> findActiveByPlate(String plate);

    long countActiveByLot(UUID lotId);

    VehicleEntry saveEntry(VehicleEntry entry);

    void deleteEntry(VehicleEntry entry);

    VehicleHistory saveHistory(VehicleHistory history);

    List<VehicleEntry> searchActiveByLotAndPlateLike(ParkingLot lot, String like);

    // Apoyos para indicadores/fees:
    BigDecimal sumHistoryBetween(UUID lotId, Instant from, Instant to);
}
