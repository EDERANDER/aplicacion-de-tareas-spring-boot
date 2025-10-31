package com.alura.apptareas.service;

import com.alura.apptareas.model.Tarea;
import com.alura.apptareas.repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@EnableAsync // ⚡ habilita ejecuciones asíncronas
public class RecordatorioScheduler {

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private EmailService emailService;

    // evita solapamientossss: ejecuta la siguiente solo cuando acabooo
    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void revisarTareasPendientes() {
        LocalDateTime ahora = LocalDateTime.now(ZoneId.of("America/Lima")).truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime inicio = ahora;
        LocalDateTime fin = ahora.plusMinutes(1);

        List<Tarea> tareasPendientes = tareaRepository.findTareasPendientesParaNotificar(inicio, fin);

        if (tareasPendientes.isEmpty()) {
            System.out.println("[" + ahora + "] No hay tareas pendientes.");
            return;
        }
        System.out.println("[" + ahora + "] Tareas pendientes encontradas: " + tareasPendientes.size());
        for (Tarea tarea : tareasPendientes) {
            enviarCorreoAsync(tarea, ahora);
        }
    }

    @Async
    public void enviarCorreoAsync(Tarea tarea, LocalDateTime ahora) {
        try {
            String asunto = "Recordatorio: " + tarea.getTitulo();
            String mensaje = "Hola " + tarea.getUsuario().getNombre() + "!\n\n"
                    + "Tienes una tarea pendiente:\n"
                    + tarea.getDescripcion() + "\n\n"
                    + "Fecha programada: " + tarea.getTimestamp();

            emailService.enviarCorreo(tarea.getUsuario().getEmail(), asunto, mensaje);

            tarea.setNotificado(true);
            tareaRepository.save(tarea);

            System.out.println("Correo enviado a " + tarea.getUsuario().getEmail() + " "
                    + ahora.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } catch (Exception e) {
            System.err.println("Error al enviar correo para tarea ID " + tarea.getId() + ": " + e.getMessage());
        }
    }
}
