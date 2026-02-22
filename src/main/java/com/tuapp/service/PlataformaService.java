package com.tuapp.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.tuapp.domain.Plataforma;
import com.tuapp.repository.PlataformaRepository;

// Servicio de negocio para gestionar plataformas disponibles en el sistema.
@Service
public class PlataformaService {

    // =========================
    // ZONA 1: DEPENDENCIAS
    // =========================
    private final PlataformaRepository plataformaRepository;

    // =========================
    // ZONA 2: CONSTRUCTOR
    // =========================
    public PlataformaService(PlataformaRepository plataformaRepository) {
        this.plataformaRepository = plataformaRepository;
    }

    // =========================
    // ZONA 3: OPERACIONES DE NEGOCIO
    // =========================
    public Plataforma crearPlataforma(Plataforma plataforma) {
        return plataformaRepository.save(plataforma);
    }

    public List<Plataforma> listarPlataformas() {
        return plataformaRepository.findAll();
    }

    public Plataforma obtenerPlataformaPorId(Long id) {
        return plataformaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Plataforma no encontrada"));
    }

    public Plataforma actualizarPlataforma(Long id, Plataforma plataformaActualizada) {
        Plataforma existente = plataformaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Plataforma no encontrada"));

        existente.setNombre(plataformaActualizada.getNombre());
        return plataformaRepository.save(existente);
    }

    public void eliminarPlataforma(Long id) {
        if (!plataformaRepository.existsById(id)) {
            throw new NoSuchElementException("Plataforma no encontrada");
        }
        plataformaRepository.deleteById(id);
    }
}
