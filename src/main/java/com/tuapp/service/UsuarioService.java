package com.tuapp.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.tuapp.domain.Usuario;
import com.tuapp.repository.UsuarioRepository;

// Servicio de negocio para gestionar usuarios del sistema.
@Service
public class UsuarioService {

    // =========================
    // ZONA 1: DEPENDENCIAS
    // =========================
    private final UsuarioRepository usuarioRepository;

    // =========================
    // ZONA 2: CONSTRUCTOR
    // =========================
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // =========================
    // ZONA 3: OPERACIONES DE NEGOCIO
    // =========================
    public Usuario crearUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));
    }

    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));

        existente.setNombre(usuarioActualizado.getNombre());
        existente.setEmail(usuarioActualizado.getEmail());
        return usuarioRepository.save(existente);
    }

    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new NoSuchElementException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }
}
