package com.tuapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tuapp.domain.Plataforma;
import com.tuapp.service.PlataformaService;

// Controlador REST para gestionar plataformas: crear, listar, consultar y eliminar.
@RestController
@RequestMapping("/plataformas")
public class PlataformaController {

    // =========================
    // ZONA 1: DEPENDENCIAS
    // =========================
    private final PlataformaService plataformaService;

    // =========================
    // ZONA 2: CONSTRUCTOR
    // =========================
    public PlataformaController(PlataformaService plataformaService) {
        this.plataformaService = plataformaService;
    }

    // =========================
    // ZONA 3: ENDPOINTS
    // =========================
    @PostMapping
    public ResponseEntity<Plataforma> crearPlataforma(@RequestBody Plataforma plataforma) {
        Plataforma creada = plataformaService.crearPlataforma(plataforma);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping
    public List<Plataforma> listarPlataformas() {
        return plataformaService.listarPlataformas();
    }

    @GetMapping("/{id}")
    public Plataforma obtenerPlataformaPorId(@PathVariable Long id) {
        return plataformaService.obtenerPlataformaPorId(id);
    }

    @PutMapping("/{id}")
    public Plataforma actualizarPlataforma(@PathVariable Long id, @RequestBody Plataforma plataforma) {
        return plataformaService.actualizarPlataforma(id, plataforma);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarPlataforma(@PathVariable Long id) {
        plataformaService.eliminarPlataforma(id);
    }

    // =========================
    // ZONA 4: MANEJO DE EXCEPCIONES
    // =========================
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoSuchElement(NoSuchElementException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return error;
    }
}
