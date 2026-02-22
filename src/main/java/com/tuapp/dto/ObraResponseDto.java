package com.tuapp.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.tuapp.domain.Estado;
import com.tuapp.domain.TipoObra;

// DTO de salida para devolver la información de una obra en respuestas de la API.
public class ObraResponseDto {

    // =========================
    // ZONA 1: DATOS DE RESPUESTA
    // =========================
    private Long id;
    private String titulo;
    private TipoObra tipo;
    private String descripcion;
    private LocalDate fechaLanzamiento;
    private LocalDateTime fechaRegistro;
    private Estado estado;
    private boolean tieneResenas;
    private List<ResenaResumenDto> resenas;
    private Long creadorId;
    private String plataformaNombre;

    // =========================
    // ZONA 2: CONSTRUCTOR
    // =========================
    public ObraResponseDto() {
    }

    // =========================
    // ZONA 3: GETTERS Y SETTERS
    // =========================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public TipoObra getTipo() {
        return tipo;
    }

    public void setTipo(TipoObra tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public void setFechaLanzamiento(LocalDate fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public boolean isTieneResenas() {
        return tieneResenas;
    }

    public void setTieneResenas(boolean tieneResenas) {
        this.tieneResenas = tieneResenas;
    }

    public List<ResenaResumenDto> getResenas() {
        return resenas;
    }

    public void setResenas(List<ResenaResumenDto> resenas) {
        this.resenas = resenas;
    }

    public Long getCreadorId() {
        return creadorId;
    }

    public void setCreadorId(Long creadorId) {
        this.creadorId = creadorId;
    }

    public String getPlataformaNombre() {
        return plataformaNombre;
    }

    public void setPlataformaNombre(String plataformaNombre) {
        this.plataformaNombre = plataformaNombre;
    }
}
