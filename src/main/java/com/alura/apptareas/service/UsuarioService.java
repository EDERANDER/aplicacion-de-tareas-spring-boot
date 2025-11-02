package com.alura.apptareas.service;

import com.alura.apptareas.dto.UsuarioDto;
import com.alura.apptareas.model.Usuario;
import com.alura.apptareas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario crearUsuario(UsuarioDto usuario){
        Usuario usuarioNuevo = new Usuario();
        usuarioNuevo.setNombre(usuario.nombre());
        usuarioNuevo.setEmail(usuario.email());
        usuarioNuevo.setContraseña(passwordEncoder.encode(usuario.contraseña()));
        usuarioNuevo.setId(usuario.id());
        return usuarioRepository.save(usuarioNuevo);
    }

    public void eliminarPerfil(Long id){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuarioRepository.deleteById(usuario.getId());
    }

    public Usuario login(String email, String contraseña) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (passwordEncoder.matches(contraseña, usuario.getContraseña())) {
            return usuario;
        } else {
            throw new RuntimeException("Contraseña incorrecta");
        }
    }
}
