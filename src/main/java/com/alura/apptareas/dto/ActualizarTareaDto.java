package com.alura.apptareas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;

import java.time.LocalDateTime;

public record ActualizarTareaDto(
        String titulo,
        String descripcion,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        @FutureOrPresent
        LocalDateTime recordatorio,
        Boolean estadoTarea
) {}