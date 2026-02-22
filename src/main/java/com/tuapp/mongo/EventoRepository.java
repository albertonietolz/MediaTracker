package com.tuapp.mongo;

import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

// Repositorio Mongo para consultar eventos por usuario, entidad o rango temporal.
public interface EventoRepository extends MongoRepository<Evento, String> {

    // =========================
    // ZONA 1: CONSULTAS POR CRITERIO
    // =========================
    List<Evento> findByUserId(Long userId);

    List<Evento> findByEntityId(Long entityId);

    List<Evento> findByTimestampBetween(Instant inicio, Instant fin);
}
