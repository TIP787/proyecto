package com.example.gestion_biblioteca.Ingresos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Ingresos implements Serializable {

    @SerializedName("ID")
    private Integer id;

    @SerializedName("MOTIVO")
    private String motivo;

    @SerializedName("MONTO")
    private String monto;

    @SerializedName("OBSERVACIONES")
    private String observaciones;

    @SerializedName("FECHA")
    private String fecha;

    public Ingresos() {}

    public Ingresos(Integer id, String motivo, String monto, String observaciones, String fecha) {
        this.id = id;
        this.motivo = motivo;
        this.monto = monto;
        this.observaciones = observaciones;
        this.fecha = fecha;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }


    public String getMotivo() {
        return motivo;
    }
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getMonto() {
        return monto;
    }
    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

}
