package com.alura.apptareas.controller;

import com.alura.apptareas.dto.LoginRequest;
import com.alura.apptareas.dto.UsuarioDto;
import com.alura.apptareas.model.Usuario;
import com.alura.apptareas.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/crearUsuario")
    public Usuario crear(@Valid @RequestBody UsuarioDto usuario){
        return usuarioService.crearUsuario(usuario);
    }

    @DeleteMapping("/eliminarUsuario/{id}")
    public void eliminar(@PathVariable Long id){
        usuarioService.eliminarPerfil(id);
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@Valid @RequestBody LoginRequest loginRequest) {
        Usuario usuario = usuarioService.login(loginRequest.email(), loginRequest.contraseña());
        return ResponseEntity.ok(usuario);
    }
}
