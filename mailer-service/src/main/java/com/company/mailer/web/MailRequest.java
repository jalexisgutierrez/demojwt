package com.company.mailer.web;

public record MailRequest(
        String email,
        String placa,
        String mensaje,
        String parqueaderoNombre
) {}