package com.tuapp.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tuapp.domain.Estado;
import com.tuapp.domain.Obra;
import com.tuapp.domain.TipoObra;

// Repositorio JPA para acceder y filtrar obras por tipo, estado y métricas asociadas.
public interface ObraRepository extends JpaRepository<Obra, Long> {

    // =========================
    // ZONA 1: CONSULTAS DERIVADAS
    // =========================
    Page<Obra> findByTipo(TipoObra tipo, Pageable pageable);

    List<Obra> findByEstado(Estado estado);

    Page<Obra> findByEstado(Estado estado, Pageable pageable);

    Page<Obra> findByTipoAndEstado(TipoObra tipo, Estado estado, Pageable pageable);

    Page<Obra> findByCreadorId(Long creadorId, Pageable pageable);

    Page<Obra> findByTipoAndCreadorId(TipoObra tipo, Long creadorId, Pageable pageable);

    Page<Obra> findByEstadoAndCreadorId(Estado estado, Long creadorId, Pageable pageable);

    Page<Obra> findByTipoAndEstadoAndCreadorId(TipoObra tipo, Estado estado, Long creadorId, Pageable pageable);

    // =========================
    // ZONA 2: CONSULTAS PERSONALIZADAS
    // =========================
    @Query("select avg(r.nota) from Resena r where r.obra.tipo = :tipo")
    Double mediaNotaPorTipo(@Param("tipo") TipoObra tipo);

    @Query("select distinct o from Obra o join Resena r on r.obra.id = o.id")
    List<Obra> findObrasConResenas();
}
