package com.company.parking.domain.port;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IndicatorsPort {

    List<Object[]> top10All();

    List<Object[]> top10ByLot(UUID lotId);

    List<String> firstTimers(UUID lotId);

    BigDecimal sumHistoryBetween(UUID lotId, Instant from, Instant to);
}
