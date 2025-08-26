package com.company.parking.adapters.out.persistence;

import com.company.parking.domain.model.VehicleHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public interface VehicleHistoryRepository extends JpaRepository<VehicleHistory, UUID> {

    @Query(value = """
    select coalesce(sum(total_cost),0)
    from vehicle_history
    where parking_lot_id = :lotId
      and exited_at >= :from and exited_at < :to
  """, nativeQuery = true)
    BigDecimal sumBetween(UUID lotId, Instant from, Instant to);
}
