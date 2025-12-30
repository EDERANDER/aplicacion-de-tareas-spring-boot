package com.alura.apptareas.controller;

import com.alura.apptareas.dto.ActualizarTareaDto;
import com.alura.apptareas.dto.TareaDto;
import com.alura.apptareas.model.Tarea;
import com.alura.apptareas.service.TareaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @DeleteMapping("/eliminarTodo/{idUsuario}")
    public void eliminarTodo(@PathVariable Long idUsuario){
        tareaService.eliminarTodasLasTareas(idUsuario);
    }

    @PostMapping("/crearTarea/{idUsuario}")
    public Tarea crearTarea(@PathVariable Long idUsuario,
                            @Valid @RequestBody TareaDto tarea) {
        return tareaService.crearTarea(idUsuario, tarea);
    }

    @PutMapping("/actualizarTarea/{idUsuario}/{idTarea}")
    public Tarea actualizarTarea(@PathVariable Long idUsuario,
                                 @PathVariable Long idTarea,
                                @RequestBody @Valid ActualizarTareaDto tarea) {
        return tareaService.actualizarTarea(idUsuario, idTarea, tarea);
    }

    @DeleteMapping("/eliminarTarea/{idUsuario}/{idTarea}")
    public ResponseEntity<Void> eliminarTarea(@PathVariable Long idUsuario, @PathVariable Long idTarea) {
        tareaService.eliminarTarea(idUsuario, idTarea);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/listaTareas/{idUsuario}")
    public List<Tarea> listaTareas(@PathVariable Long idUsuario) {
        return tareaService.listaTareas(idUsuario);
    }
}