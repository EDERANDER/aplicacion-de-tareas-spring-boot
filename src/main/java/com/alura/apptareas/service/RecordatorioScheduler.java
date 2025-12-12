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

    @Autowired WhatsappService whatsappService;

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
            String mensaje =
                            "📚 *NUEVA TAREA* \n\n" +
                            "👋 Hola *" + tarea.getUsuario().getNombre() + "*!\n\n" +
                            "Tienes una tarea pendiente. Aquí están los detalles:\n\n" +
                            "📝 *Título:*\n" +
                            "➡️ " + tarea.getTitulo() + "\n\n" +
                            "📄 *Descripción:*\n" +
                            "➡️ " + tarea.getDescripcion() + "\n\n" +
                            "📅 *Fecha programada:*\n" +
                            "➡️ " + tarea.getRecordatorio().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "\n\n" +
                            "🔔 *No olvides completarla.* ¡Tú puedes! 💪😄\n\n";


            whatsappService.enviarMensaje(tarea.getUsuario().getNumeroWhatsapp(), mensaje);

            tarea.setNotificado(true);
            tareaRepository.save(tarea);

            System.out.println("MENSAJE ENVIADO " + tarea.getUsuario().getEmail() + " "
                    + ahora.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } catch (Exception e) {
            System.out.println("Error al enviar mensaje para tarea ID " + tarea.getId() + ": " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }
}