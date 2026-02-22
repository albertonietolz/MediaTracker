package com.tuapp.dto;

import java.time.LocalDate;

import com.tuapp.domain.Estado;
import com.tuapp.domain.TipoObra;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// DTO de entrada para crear o actualizar una obra desde la API.
public class ObraDto {

    // =========================
    // ZONA 1: DATOS DE ENTRADA
    // =========================
    @NotBlank
    private String titulo;

    @NotNull
    private TipoObra tipo;

    private String descripcion;

    private LocalDate fechaLanzamiento;

    @NotNull
    private Estado estado;

    @NotNull
    private Long usuarioId;

    @NotBlank
    private String plataformaNombre;

    // =========================
    // ZONA 2: CONSTRUCTOR
    // =========================
    public ObraDto() {
    }

    // =========================
    // ZONA 3: GETTERS Y SETTERS
    // =========================
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

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getPlataformaNombre() {
        return plataformaNombre;
    }

    public void setPlataformaNombre(String plataformaNombre) {
        this.plataformaNombre = plataformaNombre;
    }
}
