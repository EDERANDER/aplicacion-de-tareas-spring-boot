package com.alura.apptareas.service;

import com.alura.apptareas.dto.ActualizarTareaDto;
import com.alura.apptareas.dto.TareaDto;
import com.alura.apptareas.model.Tarea;
import com.alura.apptareas.model.Usuario;
import com.alura.apptareas.repository.TareaRepository;
import com.alura.apptareas.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TareaService {

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Tarea crearTarea(Long idUsuario, TareaDto tarea) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro usuario con id = " + idUsuario));

        Tarea tareaModel = new Tarea();
        tareaModel.setTitulo(tarea.titulo());
        tareaModel.setDescripcion(tarea.descripcion());
        tareaModel.setRecordatorio(tarea.recordatorio());
        tareaModel.setEstadoTarea(tarea.estadoTarea());
        tareaModel.setUsuario(usuario);

        usuario.addTarea(tareaModel);
        return tareaRepository.save(tareaModel);
    }

    @Transactional
    public Tarea actualizarTarea(Long idUsuario, Long idTarea, ActualizarTareaDto tareaActualizada) {
        usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro usuario con id = " + idUsuario));

        Tarea tareaExistente = tareaRepository.findById(idTarea)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la tarea con id = " + idTarea));

        if (!tareaExistente.getUsuario().getId().equals(idUsuario)) {
            throw new SecurityException("Acción no permitida: La tarea no pertenece al usuario especificado.");
        }
        if (tareaActualizada.titulo() != null) {
            tareaExistente.setTitulo(tareaActualizada.titulo());
        }
        if (tareaActualizada.descripcion() != null) {
            tareaExistente.setDescripcion(tareaActualizada.descripcion());
        }
        if (tareaActualizada.recordatorio() != null) {
            tareaExistente.setRecordatorio(tareaActualizada.recordatorio());
        }
        if (tareaActualizada.estadoTarea() != null) {
            tareaExistente.setEstadoTarea(tareaActualizada.estadoTarea());
        }
        return tareaRepository.save(tareaExistente);
    }

    @Transactional
    public void eliminarTarea(Long idUsuario, Long idTarea) {
        Tarea tarea = tareaRepository.findById(idTarea)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la tarea con id = " + idTarea));

        if (!tarea.getUsuario().getId().equals(idUsuario)) {
            throw new SecurityException("Acción no permitida: La tarea no pertenece al usuario especificado.");
        }

        tareaRepository.delete(tarea);
    }

    @Transactional(readOnly = true)
    public List<Tarea> listaTareas(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro usuario con id = " + idUsuario));
        return usuario.getTareas();
    }
}