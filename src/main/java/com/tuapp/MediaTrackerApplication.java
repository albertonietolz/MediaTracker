package com.tuapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Clase principal que inicia la aplicación Spring Boot MediaTracker.
@SpringBootApplication
public class MediaTrackerApplication {

    // =========================
    // ZONA 1: PUNTO DE ENTRADA
    // =========================
    public static void main(String[] args) {
        SpringApplication.run(MediaTrackerApplication.class, args);
    }
}
