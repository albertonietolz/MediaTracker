package com.tuapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tuapp.domain.Plataforma;

// Repositorio JPA para gestionar plataformas y búsquedas por nombre.
public interface PlataformaRepository extends JpaRepository<Plataforma, Long> {

    // =========================
    // ZONA 1: CONSULTAS DERIVADAS
    // =========================
    Optional<Plataforma> findByNombre(String nombre);
}
