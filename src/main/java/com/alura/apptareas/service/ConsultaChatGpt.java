package com.alura.apptareas.service;

import com.alura.apptareas.model.Usuario;
import com.alura.apptareas.repository.UsuarioRepository;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ConsultaChatGpt {

    @Value("${TOKEN_OPENAI}")
    private String token;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public String consultaIa(String texto, Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró usuario"));

        // Si el usuario tiene tareas, las incluimos como contexto
        String tareasTexto = (usuario.getTareas() == null || usuario.getTareas().isEmpty())
                ? "No tiene tareas registradas."
                : usuario.getTareas().stream()
                .map(t -> {
                    String titulo = t.getTitulo() == null ? "" : t.getTitulo();
                    String desc = t.getDescripcion() == null ? "" : t.getDescripcion();
                    return "- " + titulo + ": " + desc;
                })
                .collect(Collectors.joining("\n"));

        String prompt = """
                Eres un asistente personal llamado Worki Work, integrado en una aplicación de gestión de tareas.
                
                Tu función es ayudar al usuario con:
                - Organización de tareas
                - Recordatorios y planificación
                - Consejos de estudio y productividad
                - Apoyo general en temas cotidianos y tecnológicos
                
                Siempre debes:
                - Responder únicamente en español
                - Usar un lenguaje claro, natural y directo
                - Basarte en el contexto del usuario cuando esté disponible
                - Mantener las respuestas breves y útiles
                - Entregar la respuesta exclusivamente en texto plano
                - No hacer preguntas adicionales
                - No agregar comentarios, aclaraciones ni texto innecesario
                
                Contexto del usuario:
                %s
                
                Consulta del usuario:
                %s
                
                        """.formatted(tareasTexto, texto);
        OpenAiService service = new OpenAiService(token);
        CompletionRequest requisicion = CompletionRequest.builder()
                .model("gpt-3.5-turbo-instruct") // o "gpt-4-turbo" si puedes
                .prompt(prompt)
                .maxTokens(800)
                .temperature(0.7)
                .build();

        var respuesta = service.createCompletion(requisicion);
        return respuesta.getChoices().get(0).getText().trim();
    }
}
