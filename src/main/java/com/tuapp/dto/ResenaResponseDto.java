package com.tuapp.dto;

import java.time.LocalDateTime;

// DTO de salida para devolver la información de una reseña en respuestas de la API.
public class ResenaResponseDto {

    // =========================
    // ZONA 1: DATOS DE RESPUESTA
    // =========================
    private Long id;
    private Long usuarioId;
    private Long obraId;
    private Integer nota;
    private String comentario;
    private LocalDateTime fecha;

    // =========================
    // ZONA 2: CONSTRUCTOR
    // =========================
    public ResenaResponseDto() {
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

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getObraId() {
        return obraId;
    }

    public void setObraId(Long obraId) {
        this.obraId = obraId;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
