package com.tuapp.mongo;

import java.time.Instant;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Documento MongoDB que representa un evento de auditoría o actividad del sistema.
@Document(collection = "eventos")
public class Evento {

    // =========================
    // ZONA 1: IDENTIFICADOR
    // =========================
    @Id
    private String id;

    // =========================
    // ZONA 2: DATOS DEL EVENTO
    // =========================
    private Instant timestamp;

    private Long userId;

    private String entityType;

    private Long entityId;

    private String type;

    private Map<String, Object> payload;

    // =========================
    // ZONA 3: CONSTRUCTOR
    // =========================
    public Evento() {
    }

    // =========================
    // ZONA 4: GETTERS Y SETTERS
    // =========================
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }
}
