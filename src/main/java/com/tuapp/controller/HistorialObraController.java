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

import com.tuapp.dto.CambioPorAccionDto;
import com.tuapp.mongo.HistorialObra;
import com.tuapp.service.HistorialObraService;

// Controlador REST para consultar historial de cambios de obras almacenado en MongoDB.
@RestController
@RequestMapping("/historial-obras")
public class HistorialObraController {

    private final HistorialObraService historialObraService;

    public HistorialObraController(HistorialObraService historialObraService) {
        this.historialObraService = historialObraService;
    }

    @GetMapping("/obra/{obraId}")
    public List<HistorialObra> listarPorObra(@PathVariable Long obraId) {
        return historialObraService.listarPorObra(obraId);
    }

    @GetMapping("/usuario/{userId}")
    public List<HistorialObra> listarPorUsuario(@PathVariable Long userId) {
        return historialObraService.listarPorUsuario(userId);
    }

    @GetMapping("/rango")
    public List<HistorialObra> listarPorRangoFechas(
            @RequestParam("inicio") @DateTimeFormat(iso = ISO.DATE_TIME) Instant inicio,
            @RequestParam("fin") @DateTimeFormat(iso = ISO.DATE_TIME) Instant fin) {
        return historialObraService.listarPorRangoFechas(inicio, fin);
    }

    @GetMapping("/metricas/cambios-por-accion")
    public List<CambioPorAccionDto> contarCambiosPorAccion() {
        return historialObraService.contarCambiosPorAccion();
    }
}
