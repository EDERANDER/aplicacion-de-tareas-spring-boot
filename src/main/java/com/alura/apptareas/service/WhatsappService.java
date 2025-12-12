package com.alura.apptareas.service;

import com.alura.apptareas.model.WhatsAppRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
public class WhatsappService {

    private WebClient webClient = WebClient.create();

    private final String URL_BASE = "https://apiwsp.factiliza.com/v1/message/sendtext/NTE5MTA5NTkyMzA=";
    private final String BEARER_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIzODg0MiIsImh0dHA6Ly9zY2hlbWFzLm1pY3Jvc29mdC5jb20vd3MvMjAwOC8wNi9pZGVudGl0eS9jbGFpbXMvcm9sZSI6ImNvbnN1bHRvciJ9.IhH9bpi5lvjDgflrvh1Ry5crQz-yMYBucUNsTfy6KRs";

    public boolean enviarMensaje(String numeroWhatsapp, String mensaje){
        WhatsAppRequest requestBody = new WhatsAppRequest("51"+numeroWhatsapp, mensaje);
        try {
            this.webClient.post()
                    .uri(URL_BASE)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + BEARER_TOKEN)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            clientResponse -> Mono.error(new RuntimeException("Error al enviar WhatsApp: " + clientResponse.statusCode())))
                    .toBodilessEntity()
                    .block();
            return true;
        } catch (Exception e) {
            System.err.println("Error en el servicio de WhatsApp: " + e.getMessage());
            return false;
        }
    }
}
