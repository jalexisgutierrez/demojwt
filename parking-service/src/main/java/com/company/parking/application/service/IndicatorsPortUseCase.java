package com.company.parking.application.service;

import com.company.parking.domain.port.IndicatorsPort;
import com.company.parking.domain.port.ParkingLotPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IndicatorsPortUseCase {

    private final IndicatorsPort indicatorsPort;
    private final ParkingLotPort lotPort;

    public List<Object[]> top10VehiclesAll() { return indicatorsPort.top10All(); }

    public List<Object[]> top10VehiclesByLot(UUID lotId, UUID ownerUserId) {
        lotPort.findByIdAndOwner(lotId, ownerUserId)
                .orElseThrow(() -> new IllegalArgumentException("Parking lot not found or not owned"));
        return indicatorsPort.top10ByLot(lotId);
    }

    public List<String> firstTimersInLot(UUID lotId, UUID ownerUserId) {
        lotPort.findByIdAndOwner(lotId, ownerUserId)
                .orElseThrow(() -> new IllegalArgumentException("Parking lot not found or not owned"));
        return indicatorsPort.firstTimers(lotId);
    }

    public record Earnings(java.math.BigDecimal today, java.math.BigDecimal week,
                           java.math.BigDecimal month, java.math.BigDecimal year) {}

    public Earnings earnings(UUID lotId, UUID ownerUserId, LocalDate today) {
        lotPort.findByIdAndOwner(lotId, ownerUserId)
                .orElseThrow(() -> new IllegalArgumentException("Parking lot not found or not owned"));

        ZoneId zone = ZoneId.systemDefault();
        LocalDate d = (today != null) ? today : LocalDate.now(zone);

        var startOfDay   = d.atStartOfDay(zone).toInstant();
        var endOfDay     = d.plusDays(1).atStartOfDay(zone).toInstant();
        var startOfWeek  = d.with(DayOfWeek.MONDAY).atStartOfDay(zone).toInstant();
        var endOfWeek    = d.with(DayOfWeek.SUNDAY).plusDays(1).atStartOfDay(zone).toInstant();
        var startOfMonth = d.withDayOfMonth(1).atStartOfDay(zone).toInstant();
        var endOfMonth   = d.plusMonths(1).withDayOfMonth(1).atStartOfDay(zone).toInstant();
        var startOfYear  = d.withDayOfYear(1).atStartOfDay(zone).toInstant();
        var endOfYear    = d.plusYears(1).withDayOfYear(1).atStartOfDay(zone).toInstant();

        var day   = indicatorsPort.sumHistoryBetween(lotId, startOfDay, endOfDay);
        var week  = indicatorsPort.sumHistoryBetween(lotId, startOfWeek, endOfWeek);
        var month = indicatorsPort.sumHistoryBetween(lotId, startOfMonth, endOfMonth);
        var year  = indicatorsPort.sumHistoryBetween(lotId, startOfYear, endOfYear);

        return new Earnings(day, week, month, year);
    }
}
