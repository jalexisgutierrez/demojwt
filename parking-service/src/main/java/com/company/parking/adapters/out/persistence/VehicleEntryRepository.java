package com.company.parking.adapters.out.persistence;

import com.company.parking.domain.model.ParkingLot;
import com.company.parking.domain.model.VehicleEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VehicleEntryRepository extends JpaRepository<VehicleEntry, UUID> {

    Optional<VehicleEntry> findByPlate(String plate);

    long countByParkingLot_Id(UUID lotId);

    List<VehicleEntry> findByParkingLotAndPlateContainingIgnoreCase(ParkingLot lot, String like);

    @Query(value = """
    select plate, count(*) as cnt
    from (
      select plate from vehicle_entry
      union all
      select plate from vehicle_history
    ) t
    group by plate
    order by cnt desc
    limit 10
  """, nativeQuery = true)
    List<Object[]> top10All();

    @Query(value = """
    select plate, count(*) as cnt
    from (
      select plate from vehicle_entry where parking_lot_id = :lotId
      union all
      select plate from vehicle_history where parking_lot_id = :lotId
    ) t
    group by plate
    order by cnt desc
    limit 10
  """, nativeQuery = true)
    List<Object[]> top10ByLot(UUID lotId);

    @Query(value = """
    select e.plate
    from vehicle_entry e
    where e.parking_lot_id = :lotId
      and not exists (
         select 1 from vehicle_history h
         where h.parking_lot_id = e.parking_lot_id
           and h.plate = e.plate
      )
  """, nativeQuery = true)
    List<String> firstTimers(UUID lotId);

}
