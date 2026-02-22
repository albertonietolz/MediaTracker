package com.tuapp.dto;

// DTO auxiliar para exponer opciones simples (id y nombre) en listas de selección.
public class OpcionDto {

    // =========================
    // ZONA 1: DATOS DE OPCIÓN
    // =========================
    private Long id;
    private String nombre;

    // =========================
    // ZONA 2: CONSTRUCTORES
    // =========================
    public OpcionDto() {
    }

    public OpcionDto(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
