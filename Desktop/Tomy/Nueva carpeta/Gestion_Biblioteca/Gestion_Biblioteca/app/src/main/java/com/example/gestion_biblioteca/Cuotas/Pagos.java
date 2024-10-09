package com.example.gestion_biblioteca.Cuotas;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Pagos implements Serializable {

    @SerializedName("ID")
    private Integer id;

    @SerializedName("NOMBRE")
    private String nombre;

    @SerializedName("FECHA")
    private String fecha;

    @SerializedName("MONTO")
    private String monto;

    public Pagos(){}

    public Pagos(Integer id, String nombre, String fecha, String monto) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.monto = monto;
    }


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getMonto() {
        return monto;
    }
    public void setMonto(String monto) { this.monto = monto;}
}