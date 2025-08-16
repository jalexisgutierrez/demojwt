package com.company.parking.domain.port;

public interface MailerPort {

    MailResponse send(MailCommand cmd);

    record MailCommand(String email, String placa, String mensaje, String parqueaderoNombre) {}
    record MailResponse(String mensaje) {}
}
