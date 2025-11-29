package com.levelup.backendapi.controller;

import com.levelup.backendapi.model.Usuario;
import com.levelup.backendapi.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // Importante
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
// ESTO ES VITAL: Permite que tu React (puerto 3000) se comunique con Java
@CrossOrigin(origins = {"http://localhost:3000", "http://18.206.237.91", "http://18.206.237.91:3000"})
@Tag(name = "Usuarios", description = "Gestión de usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    @Operation(summary = "Listar todos los usuarios")
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo usuario (Registro)")
    public Usuario createUsuario(@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // --- NUEVO: MÉTODO PARA LOGIN SIMPLE ---
    @PostMapping("/login")
    @Operation(summary = "Verificar credenciales de usuario")
    public ResponseEntity<?> login(@RequestBody Usuario usuarioLogin) {
        // Buscamos por nombre de usuario
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByUsuario(usuarioLogin.getUsuario());
        
        if (usuarioEncontrado.isPresent()) {
            // Verificamos la contraseña (En un caso real, aquí deberías comparar hashes, no texto plano)
            if (usuarioEncontrado.get().getPassword().equals(usuarioLogin.getPassword())) {
                return ResponseEntity.ok(usuarioEncontrado.get());
            } else {
                return ResponseEntity.status(401).body("Contraseña incorrecta");
            }
        }
        return ResponseEntity.status(404).body("Usuario no encontrado");
    }
    // ---------------------------------------

    @GetMapping("/{id}")
    public Usuario getUsuarioById(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @PutMapping("/{id}")
    public Usuario updateUsuario(@PathVariable Long id, @RequestBody Usuario usuarioDetalles) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombreCompleto(usuarioDetalles.getNombreCompleto());
        usuario.setCorreo(usuarioDetalles.getCorreo());
        usuario.setUsuario(usuarioDetalles.getUsuario());
        usuario.setPassword(usuarioDetalles.getPassword());

        return usuarioRepository.save(usuario);
    }

    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
    }
}