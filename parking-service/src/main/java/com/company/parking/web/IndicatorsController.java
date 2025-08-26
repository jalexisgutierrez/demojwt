package com.company.parking.web;

import com.company.parking.adapters.in.security.JwtAuthFilter;
import com.company.parking.application.service.IndicatorsPortUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/indicators")
public class IndicatorsController {

    private final IndicatorsPortUseCase useCase;

    public IndicatorsController(IndicatorsPortUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping("/top10/all")
    public ResponseEntity<List<Object[]>> top10All() { return ResponseEntity.ok(useCase.top10VehiclesAll()); }

    @GetMapping("/top10/lot/{lotId}")
    public ResponseEntity<List<Object[]>> top10Lot(@PathVariable UUID lotId,
                                                   @AuthenticationPrincipal JwtAuthFilter.UserPrincipal me) {
        return ResponseEntity.ok(useCase.top10VehiclesByLot(lotId, UUID.fromString(me.uid())));
    }

    @GetMapping("/first-timers/{lotId}")
    public ResponseEntity<List<String>> firstTimers(@PathVariable UUID lotId,
                                                    @AuthenticationPrincipal JwtAuthFilter.UserPrincipal me) {
        return ResponseEntity.ok(useCase.firstTimersInLot(lotId, UUID.fromString(me.uid())));
    }

    @GetMapping("/earnings/{lotId}")
    public ResponseEntity<IndicatorsPortUseCase.Earnings> earnings(@PathVariable UUID lotId,
                                                                   @RequestParam(required=false) String date,
                                                                   @AuthenticationPrincipal JwtAuthFilter.UserPrincipal me) {
        LocalDate d = (date != null) ? LocalDate.parse(date) : null;
        return ResponseEntity.ok(useCase.earnings(lotId, UUID.fromString(me.uid()), d));
    }
}
