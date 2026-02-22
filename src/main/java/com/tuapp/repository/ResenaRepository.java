package com.tuapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tuapp.domain.Resena;

// Repositorio JPA para acceder a reseñas y consultarlas por obra.
public interface ResenaRepository extends JpaRepository<Resena, Long> {

    // =========================
    // ZONA 1: CONSULTAS DERIVADAS
    // =========================
    List<Resena> findByObraId(Long obraId);

    List<Resena> findByObraIdIn(List<Long> obraIds);
}
