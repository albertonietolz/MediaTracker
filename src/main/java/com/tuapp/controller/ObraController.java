package com.tuapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tuapp.domain.Estado;
import com.tuapp.domain.Obra;
import com.tuapp.domain.Resena;
import com.tuapp.domain.TipoObra;
import com.tuapp.dto.ObraDto;
import com.tuapp.dto.ObraResponseDto;
import com.tuapp.dto.ResenaResumenDto;
import com.tuapp.service.ObraService;
import com.tuapp.service.ResenaService;

import jakarta.validation.Valid;

// Controlador REST para gestionar obras: crear, actualizar, listar, consultar y eliminar.
@RestController
@RequestMapping("/obras")
public class ObraController {

    // =========================
    // ZONA 1: DEPENDENCIAS
    // =========================
    private final ObraService obraService;
    private final ResenaService resenaService;

    // =========================
    // ZONA 2: CONSTRUCTOR
    // =========================
    public ObraController(ObraService obraService, ResenaService resenaService) {
        this.obraService = obraService;
        this.resenaService = resenaService;
    }

    // =========================
    // ZONA 3: ENDPOINTS
    // =========================
    @PostMapping
    public ResponseEntity<ObraResponseDto> crearObra(@Valid @RequestBody ObraDto dto) {
        Obra creada = obraService.crearObra(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDto(creada, List.of()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ObraResponseDto> actualizarObra(@PathVariable Long id, @Valid @RequestBody ObraDto dto) {
        Obra actualizada = obraService.actualizarObra(id, dto);
        List<Resena> resenas = resenaService.listarPorObra(actualizada.getId());
        return ResponseEntity.ok(toResponseDto(actualizada, resenas));
    }

    @GetMapping
    public Page<ObraResponseDto> listarObras(
            @RequestParam(name = "tipo", required = false) TipoObra tipo,
            @RequestParam(name = "estado", required = false) Estado estado,
            @RequestParam(name = "usuarioId", required = false) Long usuarioId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Obra> obrasPage = obraService.listarObras(tipo, estado, usuarioId, pageable);
        Map<Long, List<Resena>> resenasPorObra = resenasPorObra(obrasPage.getContent());

        return obrasPage.map(obra -> toResponseDto(obra, resenasPorObra.getOrDefault(obra.getId(), List.of())));
    }

    @GetMapping("/{id}")
    public ObraResponseDto obtenerObra(@PathVariable Long id) {
        Obra obra = obraService.obtenerPorId(id);
        List<Resena> resenas = resenaService.listarPorObra(id);
        return toResponseDto(obra, resenas);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<ObraResponseDto> listarPorUsuario(@PathVariable Long usuarioId) {
        List<Obra> obras = obraService.listarObrasPorUsuario(usuarioId);
        Map<Long, List<Resena>> resenasPorObra = resenasPorObra(obras);

        return obras.stream()
                .map(obra -> toResponseDto(obra, resenasPorObra.getOrDefault(obra.getId(), List.of())))
                .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarObra(@PathVariable Long id) {
        obraService.eliminarObra(id);
        return ResponseEntity.noContent().build();
    }

    // =========================
    // ZONA 4: MAPEO A DTO
    // =========================
    private ObraResponseDto toResponseDto(Obra obra, List<Resena> resenas) {
        ObraResponseDto dto = new ObraResponseDto();
        dto.setId(obra.getId());
        dto.setTitulo(obra.getTitulo());
        dto.setTipo(obra.getTipo());
        dto.setDescripcion(obra.getDescripcion());
        dto.setFechaLanzamiento(obra.getFechaLanzamiento());
        dto.setFechaRegistro(obra.getFechaRegistro());
        dto.setEstado(obra.getEstado());
        dto.setTieneResenas(!resenas.isEmpty());
        dto.setResenas(resenas.stream().map(this::toResenaResumenDto).collect(Collectors.toList()));
        dto.setCreadorId(obra.getCreador() != null ? obra.getCreador().getId() : null);
        dto.setPlataformaNombre(obra.getPlataforma() != null ? obra.getPlataforma().getNombre() : null);
        return dto;
    }

    private ResenaResumenDto toResenaResumenDto(Resena resena) {
        ResenaResumenDto dto = new ResenaResumenDto();
        dto.setId(resena.getId());
        dto.setNota(resena.getNota());
        dto.setComentario(resena.getComentario());
        dto.setUsuarioId(resena.getUsuario() != null ? resena.getUsuario().getId() : null);
        return dto;
    }

    private Map<Long, List<Resena>> resenasPorObra(List<Obra> obras) {
        List<Long> obraIds = obras.stream().map(Obra::getId).toList();
        return resenaService.listarPorObras(obraIds).stream().collect(Collectors.groupingBy(resena -> resena.getObra().getId()));
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
