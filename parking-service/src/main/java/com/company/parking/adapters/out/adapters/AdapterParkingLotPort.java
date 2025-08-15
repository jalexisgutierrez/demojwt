package com.company.parking.adapters.out.adapters;

import com.company.parking.adapters.out.persistence.ParkingLotRepository;
import com.company.parking.domain.model.ParkingLot;
import com.company.parking.domain.port.ParkingLotPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AdapterParkingLotPort implements ParkingLotPort {

    private final ParkingLotRepository repo;

    @Override
    public ParkingLot save(ParkingLot lot) {
        return repo.save(lot);
    }

    @Override
    public Optional<ParkingLot> findById(UUID id) {
        return repo.findById(id);
    }

    @Override
    public Optional<ParkingLot> findByIdAndOwner(UUID id, UUID ownerUserId) {
        return repo.findByIdAndOwnerUserId(id, ownerUserId);
    }
}
