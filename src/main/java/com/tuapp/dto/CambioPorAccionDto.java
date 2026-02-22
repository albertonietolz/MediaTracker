package com.tuapp.dto;

// DTO de salida para agregacion de historial: cantidad de cambios por accion.
public class CambioPorAccionDto {

    private String accion;
    private long total;

    public CambioPorAccionDto() {
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
