package com.alura.apptareas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record TareaDto(
        Long id,

        @NotBlank(message = "El título no puede estar vacío")
        @Size(max = 50, message = "El título no puede superar los 50 caracteres")
        String titulo,

        @NotBlank(message = "La descripción es obligatoria")
        String descripcion,

        @NotNull(message = "Debe indicar la fecha del recordatorio")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime recordatorio,

        boolean estadoTarea
) {
}
