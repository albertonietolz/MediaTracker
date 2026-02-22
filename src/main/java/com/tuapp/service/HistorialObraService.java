package com.tuapp.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.tuapp.dto.CambioPorAccionDto;
import com.tuapp.mongo.HistorialObra;
import com.tuapp.mongo.HistorialObraRepository;

// Servicio para registrar y consultar historial de cambios de obras en MongoDB.
@Service
public class HistorialObraService {

    private final HistorialObraRepository historialObraRepository;

    public HistorialObraService(HistorialObraRepository historialObraRepository) {
        this.historialObraRepository = historialObraRepository;
    }

    public void registrarCambio(Long obraId, Long userId, String accion, Map<String, Object> antes, Map<String, Object> despues) {
        HistorialObra historial = new HistorialObra();
        historial.setObraId(obraId);
        historial.setUserId(userId);
        historial.setAccion(accion);
        historial.setTimestamp(Instant.now());
        historial.setAntes(antes);
        historial.setDespues(despues);

        historialObraRepository.save(historial);
    }

    public List<HistorialObra> listarPorObra(Long obraId) {
        return historialObraRepository.findByObraIdOrderByTimestampDesc(obraId);
    }

    public List<HistorialObra> listarPorUsuario(Long userId) {
        return historialObraRepository.findByUserIdOrderByTimestampDesc(userId);
    }

    public List<HistorialObra> listarPorRangoFechas(Instant inicio, Instant fin) {
        return historialObraRepository.findByTimestampBetweenOrderByTimestampDesc(inicio, fin);
    }

    public List<CambioPorAccionDto> contarCambiosPorAccion() {
        return historialObraRepository.contarCambiosPorAccion();
    }
}
