package com.alura.apptareas.controller;

import com.alura.apptareas.dto.ConsultaRequest;
import com.alura.apptareas.service.ConsultaChatGpt;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class IaController {
    @Autowired
    private ConsultaChatGpt consultaChatGpt;

    @GetMapping("/ia/{idUsuario}")
    public ResponseEntity<?> consultaIa(@PathVariable Long idUsuario,@Valid @RequestBody ConsultaRequest request) {
        try {
            String result = consultaChatGpt.consultaIa(request.getTexto(), idUsuario);
            return ResponseEntity.ok(Map.of(
                    "pregunta", request.getTexto(),
                    "respuesta", result
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}
