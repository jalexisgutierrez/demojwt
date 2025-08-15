package com.company.parking.domain.port;

import com.company.parking.domain.model.ParkingLot;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface ParkingLotPort {

    ParkingLot save(ParkingLot lot);

    Optional<ParkingLot> findById(UUID id);

    Optional<ParkingLot> findByIdAndOwner(UUID id, UUID ownerUserId);
}
