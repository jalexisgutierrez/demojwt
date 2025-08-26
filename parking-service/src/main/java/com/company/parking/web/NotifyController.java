package com.company.parking.web;

import com.company.parking.adapters.in.security.JwtAuthFilter;
import com.company.parking.application.service.NotifyUseCase;
import com.company.parking.domain.port.MailerPort;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/entries")
public class NotifyController {

    private final NotifyUseCase useCase;

    public NotifyController(NotifyUseCase useCase) {
        this.useCase = useCase;
    }

    public record NotifyReq(@Email String to, @NotBlank String message) {}

    @PostMapping("/{lotId}/notify/{plate}")
    public ResponseEntity<MailerPort.MailResponse> notify(
            @PathVariable UUID lotId,
            @PathVariable String plate,
            @RequestBody NotifyReq req,
            @AuthenticationPrincipal JwtAuthFilter.UserPrincipal me
    ) {
        var resp = useCase.notify(
                lotId, plate,
                UUID.fromString(me.uid()),
                req.to(),
                req.message()
        );
        return ResponseEntity.ok(resp);
    }
}
