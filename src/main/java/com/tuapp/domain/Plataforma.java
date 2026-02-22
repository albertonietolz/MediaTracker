package com.tuapp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "plataformas")
public class Plataforma {

    // =========================
    // ZONA 1: CLAVE PRIMARIA
    // =========================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // ZONA 2: DATOS PRINCIPALES
    // =========================
    @Column(nullable = false, unique = true)
    private String nombre;

    // =========================
    // ZONA 3: CONSTRUCTOR
    // =========================
    public Plataforma() {
    }

    // =========================
    // ZONA 4: GETTERS Y SETTERS
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
