package com.company.parking.web;

import com.company.parking.adapters.in.security.JwtAuthFilter;
import com.company.parking.application.service.ParkingLotPortUseCase;
import com.company.parking.domain.model.ParkingLot;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/lots")
public class ParkingLotController {

    private final ParkingLotPortUseCase useCase;
    public ParkingLotController(ParkingLotPortUseCase useCase) { this.useCase = useCase; }

    public record CreateLotReq(@NotBlank String name, @Min(1) int capacity, BigDecimal hourlyRate) {}

    @PostMapping
    public ResponseEntity<ParkingLot> create(@RequestBody CreateLotReq req,
                                             @AuthenticationPrincipal JwtAuthFilter.UserPrincipal me) {
        var lot = useCase.create(req.name(), req.capacity(), req.hourlyRate(),
                UUID.fromString(me.uid()), me.email());
        return ResponseEntity.ok(lot);
    }

    @GetMapping("/{lotId}")
    public ResponseEntity<ParkingLot> get(@PathVariable UUID lotId, @AuthenticationPrincipal JwtAuthFilter.UserPrincipal me) {
        return ResponseEntity.ok(useCase.getOwned(lotId, UUID.fromString(me.uid())));
    }

}
