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
                        Eres un asistente personal llamado AnderIa.
                        Puedes hablar sobre cualquier tema: tareas, consejos, estudio, relaciones, tecnología, etc.
                        
                        Si el usuario tiene tareas, puedes mencionarlas como contexto.
                        Responde siempre en español, de manera clara y natural.
                        No hagas preguntas al final ni agregues frases extra que no te pidan.
                        Solo responde a lo que se te pregunta.
                        
                        Contexto del usuario:
                        %s
                        
                        Pregunta del usuario:
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
