package com.alura.apptareas.dto;

import jakarta.validation.constraints.NotBlank;

public class ConsultaRequest {
    @NotBlank(message = "Debes ingresar un mensaje")
    private String texto;

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
