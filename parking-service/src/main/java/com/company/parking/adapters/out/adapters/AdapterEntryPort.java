package com.company.parking.adapters.out.adapters;

import com.company.parking.adapters.out.persistence.VehicleEntryRepository;
import com.company.parking.adapters.out.persistence.VehicleHistoryRepository;
import com.company.parking.domain.model.ParkingLot;
import com.company.parking.domain.model.VehicleEntry;
import com.company.parking.domain.model.VehicleHistory;
import com.company.parking.domain.port.EntryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AdapterEntryPort implements EntryPort {

    private final VehicleEntryRepository entryRepo;
    private final VehicleHistoryRepository historyRepo;

    @Override
    public Optional<VehicleEntry> findActiveByPlate(String plate) {
        return entryRepo.findByPlate(plate);
    }

    @Override
    public long countActiveByLot(UUID lotId) {
        return entryRepo.countByParkingLot_Id(lotId);
    }

    @Override
    public VehicleEntry saveEntry(VehicleEntry entry) {
        return entryRepo.save(entry);
    }

    @Override
    public void deleteEntry(VehicleEntry entry) {
        entryRepo.delete(entry);
    }

    @Override
    public VehicleHistory saveHistory(VehicleHistory history) {
        return historyRepo.save(history);
    }

    @Override
    public List<VehicleEntry> searchActiveByLotAndPlateLike(ParkingLot lot, String like) {
        return entryRepo.findByParkingLotAndPlateContainingIgnoreCase(lot, like);
    }

    @Override
    public BigDecimal sumHistoryBetween(UUID lotId, Instant from, Instant to) {
        return historyRepo.sumBetween(lotId, from, to);
    }
}
