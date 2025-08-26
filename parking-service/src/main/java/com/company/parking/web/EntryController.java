package com.company.parking.web;
import com.company.parking.adapters.in.security.JwtAuthFilter;
import com.company.parking.application.service.EntryPortUseCase;
import com.company.parking.web.dto.VehicleEntryDto;
import com.company.parking.web.dto.VehicleHistoryDto;
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
    public ResponseEntity<VehicleEntryDto> enter(@PathVariable UUID lotId,
                                                 @RequestBody EnterReq req,
                                                 @AuthenticationPrincipal JwtAuthFilter.UserPrincipal me) {
        var e = useCase.enter(lotId, req.plate(), UUID.fromString(me.uid()));
        return ResponseEntity.ok(VehicleEntryDto.from(e));
    }

    @PostMapping("/{lotId}/exit/{plate}")
    public ResponseEntity<VehicleHistoryDto> exit(@PathVariable UUID lotId,
                                                  @PathVariable String plate,
                                                  @AuthenticationPrincipal JwtAuthFilter.UserPrincipal me) {
        var h = useCase.exit(lotId, plate, UUID.fromString(me.uid()));
        return ResponseEntity.ok(VehicleHistoryDto.from(h));
    }

    @GetMapping("/{lotId}/search")
    public ResponseEntity<List<VehicleEntryDto>> search(@PathVariable UUID lotId,
                                                        @RequestParam("q") String q,
                                                        @AuthenticationPrincipal JwtAuthFilter.UserPrincipal me) {
        var list = useCase.searchParkedLike(lotId, q, UUID.fromString(me.uid()))
                .stream().map(VehicleEntryDto::from).toList();
        return ResponseEntity.ok(list);
    }
}
