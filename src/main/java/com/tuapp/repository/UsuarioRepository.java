package com.tuapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tuapp.domain.Usuario;

// Repositorio JPA para operaciones CRUD de usuarios.
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // =========================
    // ZONA 1: CONSULTAS BASE (HEREDADAS DE JPA)
    // =========================
}
