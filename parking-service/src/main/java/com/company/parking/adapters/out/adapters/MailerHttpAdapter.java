package com.company.parking.adapters.out.adapters;

import com.company.parking.domain.port.MailerPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class MailerHttpAdapter implements MailerPort {

    private final RestClient rest;


    public MailerHttpAdapter(RestClient.Builder builder,
                             @Value("${mailer.base-url}") String baseUrl) {
        this.rest = builder.baseUrl(baseUrl).build();
    }

    @Override
    public MailResponse send(MailCommand cmd) {
        // Llama al endpoint del mailer-service
        return rest.post()
                .uri("/mail/send")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(cmd)
                .retrieve()
                .body(MailResponse.class);
    }

}
