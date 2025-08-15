package com.company.parking.web;

import com.company.parking.adapters.in.security.JwtAuthFilter;
import com.company.parking.application.service.EntryPortUseCase;
import com.company.parking.domain.model.VehicleEntry;
import com.company.parking.domain.model.VehicleHistory;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/entries")
public class EntryController {

    private final EntryPortUseCase useCase;

    public EntryController(EntryPortUseCase useCase) {
        this.useCase = useCase;
    }

    public record EnterReq(@NotBlank String plate) {}

    @PostMapping("/{lotId}/enter")
    public ResponseEntity<VehicleEntry> enter(@PathVariable UUID lotId, @RequestBody EnterReq req,
                                              @AuthenticationPrincipal JwtAuthFilter.UserPrincipal me) {
        return ResponseEntity.ok(useCase.enter(lotId, req.plate(), UUID.fromString(me.uid())));
    }

    @PostMapping("/{lotId}/exit/{plate}")
    public ResponseEntity<VehicleHistory> exit(@PathVariable UUID lotId, @PathVariable String plate,
                                               @AuthenticationPrincipal JwtAuthFilter.UserPrincipal me) {
        return ResponseEntity.ok(useCase.exit(lotId, plate, UUID.fromString(me.uid())));
    }

    @GetMapping("/{lotId}/search")
    public ResponseEntity<List<VehicleEntry>> search(@PathVariable UUID lotId, @RequestParam("q") String q,
                                                     @AuthenticationPrincipal JwtAuthFilter.UserPrincipal me) {
        return ResponseEntity.ok(useCase.searchParkedLike(lotId, q, UUID.fromString(me.uid())));
    }
}
