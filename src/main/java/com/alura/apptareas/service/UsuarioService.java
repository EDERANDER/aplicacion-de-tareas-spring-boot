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
        Usuario usuario1 = new Usuario();
        usuario1.setNombre(usuario.nombre());
        usuario1.setEmail(usuario.email());
        usuario1.setContraseña(passwordEncoder.encode(usuario.contraseña()));
        usuario1.setId(usuario.id());
        return usuarioRepository.save(usuario1);
    }

    public void eliminarPerfil(Long id){
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if(usuario.isPresent()){
            usuarioRepository.deleteById(id);
        }else{
            throw new RuntimeException("No se encontro al usuario");
        }
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
