package com.alura.apptareas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Debe ingresar una dirección de correo")
        @Email(message = "Debe ingresar un correo válido")
        String email,

        @NotBlank(message = "Debe ingresar una contraseña")
        String contraseña
) {
}
