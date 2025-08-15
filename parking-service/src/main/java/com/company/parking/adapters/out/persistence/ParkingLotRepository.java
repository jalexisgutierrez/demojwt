package com.company.parking.adapters.out.persistence;

import com.company.parking.domain.model.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ParkingLotRepository extends JpaRepository<ParkingLot, UUID> {

    Optional<ParkingLot> findByIdAndOwnerUserId(UUID id, UUID ownerUserId);
}
