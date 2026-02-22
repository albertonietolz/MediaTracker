package com.tuapp.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

// DTO de entrada para crear o actualizar una reseña desde la API.
public class ResenaDto {

    // =========================
    // ZONA 1: DATOS DE ENTRADA
    // =========================
    @NotNull
    private Long usuarioId;

    @NotNull
    private Long obraId;

    @NotNull
    @Min(0)
    @Max(10)
    private Integer nota;

    private String comentario;

    // =========================
    // ZONA 2: CONSTRUCTOR
    // =========================
    public ResenaDto() {
    }

    // =========================
    // ZONA 3: GETTERS Y SETTERS
    // =========================
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
}
