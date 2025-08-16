package com.company.parking.application.service;

import com.company.parking.domain.model.VehicleEntry;
import com.company.parking.domain.port.EntryPort;
import com.company.parking.domain.port.MailerPort;
import com.company.parking.domain.port.ParkingLotPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotifyUseCase {

    private final EntryPort entryPort;
    private final ParkingLotPort lotPort;
    private final MailerPort mailer;

    public MailerPort.MailResponse notify(UUID lotId, String plate, UUID requesterUid,
                                          String email, String mensaje) {
        // 1) validar ownership del lote
        var lot = lotPort.findByIdAndOwner(lotId, requesterUid)
                .orElseThrow(() -> new IllegalArgumentException("Parking lot not found or not owned by user"));

        // 2) validar que la placa esté actualmente parqueada en ese lote
        VehicleEntry ve = entryPort.findActiveByLotAndPlate(lotId, plate)
                .orElseThrow(() -> new IllegalArgumentException("La placa no está actualmente parqueada en este lote"));

        // 3) armar comando y llamar mailer
        var cmd = new MailerPort.MailCommand(
                email,
                plate,
                mensaje,
                lot.getName()
        );
        return mailer.send(cmd);
    }
}
