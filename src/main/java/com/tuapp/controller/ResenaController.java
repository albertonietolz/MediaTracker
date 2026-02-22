package com.tuapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

import com.tuapp.domain.Resena;
import com.tuapp.dto.OpcionDto;
import com.tuapp.dto.ResenaDto;
import com.tuapp.dto.ResenaResponseDto;
import com.tuapp.service.ObraService;
import com.tuapp.service.ResenaService;

import jakarta.validation.Valid;

// Controlador REST para gestionar reseñas y consultar opciones de usuarios y obras.
@RestController
@RequestMapping("/resenas")
public class ResenaController {

    // =========================
    // ZONA 1: DEPENDENCIAS
    // =========================
    private final ResenaService resenaService;
    private final ObraService obraService;

    // =========================
    // ZONA 2: CONSTRUCTOR
    // =========================
    public ResenaController(ResenaService resenaService, ObraService obraService) {
        this.resenaService = resenaService;
        this.obraService = obraService;
    }

    // =========================
    // ZONA 3: ENDPOINTS
    // =========================
    @PostMapping
    public ResponseEntity<ResenaResponseDto> crearResena(@Valid @RequestBody ResenaDto dto) {
        Resena creada = resenaService.crearResena(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDto(creada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResenaResponseDto> actualizarResena(@PathVariable Long id, @Valid @RequestBody ResenaDto dto) {
        Resena actualizada = resenaService.actualizarResena(id, dto);
        return ResponseEntity.ok(toResponseDto(actualizada));
    }

    @GetMapping("/obra/{obraId}")
    public List<ResenaResponseDto> listarPorObra(@PathVariable Long obraId) {
        return resenaService.listarPorObra(obraId)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarResena(@PathVariable Long id) {
        resenaService.eliminarResena(id);
    }

    @GetMapping("/obras-con-resenas")
    public List<OpcionDto> listarObrasConResenas() {
        return obraService.listarObrasConResenas().stream()
                .map(obra -> new OpcionDto(obra.getId(), obra.getTitulo()))
                .collect(Collectors.toList());
    }

    // =========================
    // ZONA 4: MAPEO A DTO
    // =========================
    private ResenaResponseDto toResponseDto(Resena resena) {
        ResenaResponseDto dto = new ResenaResponseDto();
        dto.setId(resena.getId());
        dto.setNota(resena.getNota());
        dto.setComentario(resena.getComentario());
        dto.setFecha(resena.getFecha());
        dto.setUsuarioId(resena.getUsuario() != null ? resena.getUsuario().getId() : null);
        dto.setObraId(resena.getObra() != null ? resena.getObra().getId() : null);
        return dto;
    }

    // =========================
    // ZONA 5: MANEJO DE EXCEPCIONES
    // =========================
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoSuchElement(NoSuchElementException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return error;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
