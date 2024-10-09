package com.example.gestion_biblioteca.Gastos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Gastos implements Serializable {

    @SerializedName("ID")
    private Integer id;

    @SerializedName("FECHA")
    private String fecha;

    @SerializedName("MOTIVO")
    private String motivo;

    @SerializedName("MONTO")
    private String monto;

    @SerializedName("OBSERVACIONES")
    private String observaciones;


    public Gastos(){}

    public Gastos(Integer id,String fecha, String motivo, String monto, String observaciones) {
        this.id = id;
        this.fecha = fecha;
        this.motivo = motivo;
        this.monto = monto;
        this.observaciones = observaciones;
    }


    public Integer getId() {
       return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha){
        this.fecha = fecha;
    }

    public String getMotivo() {
        return motivo;
    }
    public void setMotivo(String motivo){
        this.motivo = motivo;
    }

    public String getMonto() {
        return monto;
    }
    public void setMonto(String monto){
        this.monto = monto;
    }

    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones){
        this.observaciones = observaciones;
    }
}
