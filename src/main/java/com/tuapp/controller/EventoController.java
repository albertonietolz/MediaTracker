package com.tuapp.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tuapp.mongo.Evento;
import com.tuapp.service.EventoService;

// Controlador REST para consultar eventos por usuario, entidad o rango de fechas.
@RestController
@RequestMapping("/eventos")
public class EventoController {

    // =========================
    // ZONA 1: DEPENDENCIAS
    // =========================
    private final EventoService eventoService;

    // =========================
    // ZONA 2: CONSTRUCTOR
    // =========================
    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    // =========================
    // ZONA 3: ENDPOINTS
    // =========================
    @GetMapping("/usuario/{userId}")
    public List<Evento> listarPorUsuario(@PathVariable Long userId) {
        return eventoService.listarPorUsuario(userId);
    }

    @GetMapping("/entidad/{entityId}")
    public List<Evento> listarPorEntidad(@PathVariable Long entityId) {
        return eventoService.listarPorEntidad(entityId);
    }

    @GetMapping("/rango")
    public List<Evento> listarPorRangoFechas(
            @RequestParam("inicio") @DateTimeFormat(iso = ISO.DATE_TIME) Instant inicio,
            @RequestParam("fin") @DateTimeFormat(iso = ISO.DATE_TIME) Instant fin) {
        return eventoService.listarPorRangoFechas(inicio, fin);
    }
}
