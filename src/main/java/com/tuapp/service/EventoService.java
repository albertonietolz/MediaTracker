package com.tuapp.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tuapp.mongo.Evento;
import com.tuapp.mongo.EventoRepository;

// Servicio de eventos para consultar actividad registrada en MongoDB por distintos criterios.
@Service
public class EventoService {

    // =========================
    // ZONA 1: DEPENDENCIAS
    // =========================
    private final EventoRepository eventoRepository;

    // =========================
    // ZONA 2: CONSTRUCTOR
    // =========================
    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    // =========================
    // ZONA 3: OPERACIONES DE CONSULTA
    // =========================
    public List<Evento> listarPorUsuario(Long userId) {
        return eventoRepository.findByUserId(userId);
    }

    public List<Evento> listarPorEntidad(Long entityId) {
        return eventoRepository.findByEntityId(entityId);
    }

    public List<Evento> listarPorRangoFechas(Instant inicio, Instant fin) {
        return eventoRepository.findByTimestampBetween(inicio, fin);
    }
}
