package com.company.parking.adapters.out.adapters;

import com.company.parking.adapters.out.persistence.VehicleEntryRepository;
import com.company.parking.adapters.out.persistence.VehicleHistoryRepository;
import com.company.parking.domain.port.IndicatorsPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AdapterIndicatorsPort implements IndicatorsPort {

    private final VehicleEntryRepository entryRepo;
    private final VehicleHistoryRepository historyRepo;

    @Override
    public List<Object[]> top10All() {
        return entryRepo.top10All();
    }

    @Override
    public List<Object[]> top10ByLot(UUID lotId) {
        return entryRepo.top10ByLot(lotId);
    }

    @Override
    public List<String> firstTimers(UUID lotId) {
        return entryRepo.firstTimers(lotId);
    }

    @Override
    public BigDecimal sumHistoryBetween(UUID lotId, Instant from, Instant to) {
        return historyRepo.sumBetween(lotId, from, to);
    }
}
