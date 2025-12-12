package com.alura.apptareas.service;

import org.springframework.stereotype.Service;

@Service
public class OtraClase {

    private final WhatsappService whatsappService;

    public OtraClase(WhatsappService whatsappService) {
        this.whatsappService = whatsappService;
    }

    public void ejecutar() {
        whatsappService.enviarMensaje("123456789", "Hola desde otra clase");
    }
}
