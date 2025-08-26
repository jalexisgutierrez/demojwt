package com.company.mailer.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class MailController {

    private static final Logger log = LoggerFactory.getLogger(MailController.class);

    @PostMapping("/send")
    public ResponseEntity<MailResponse> send(@RequestBody MailRequest req) {
        // Loguea exactamente lo que llega (como pide el enunciado)
        log.info("Mailer recibido -> email={}, placa={}, mensaje={}, parqueaderoNombre={}",
                req.email(), req.placa(), req.mensaje(), req.parqueaderoNombre());

        // Respuesta 200 con "Correo Enviado"
        return ResponseEntity.ok(new MailResponse("Correo Enviado"));
    }
}