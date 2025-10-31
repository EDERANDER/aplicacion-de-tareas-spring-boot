package com.alura.apptareas.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.util.Date;
import java.util.List;

public record UsuarioDto(

        Long id,

        @NotBlank(message = "Debe contener un nombre de usuario")
        @Size(max = 20, message = "El nombre no puede superar los 20 caracteres")
        String nombre,

        @NotBlank(message = "Debe ingresar una dirección de correo")
        @Email(message = "Debe ingresar un correo válido")
        String email,

        @NotBlank(message = "Debe ingresar una contraseña")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        String contraseña,


        @Valid
        List<TareaDto> tareas
) {}