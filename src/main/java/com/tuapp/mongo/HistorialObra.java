package com.tuapp.mongo;

import java.time.Instant;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Documento MongoDB que representa un cambio en una obra (antes/despues).
@Document(collection = "historial_obras")
public class HistorialObra {

    @Id
    private String id;

    private Long obraId;
    private Long userId;
    private Instant timestamp;
    private String accion;
    private Map<String, Object> antes;
    private Map<String, Object> despues;

    public HistorialObra() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getObraId() {
        return obraId;
    }

    public void setObraId(Long obraId) {
        this.obraId = obraId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public Map<String, Object> getAntes() {
        return antes;
    }

    public void setAntes(Map<String, Object> antes) {
        this.antes = antes;
    }

    public Map<String, Object> getDespues() {
        return despues;
    }

    public void setDespues(Map<String, Object> despues) {
        this.despues = despues;
    }
}
