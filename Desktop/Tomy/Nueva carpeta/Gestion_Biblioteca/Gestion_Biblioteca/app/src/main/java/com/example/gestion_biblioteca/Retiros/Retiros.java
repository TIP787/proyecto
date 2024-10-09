package com.example.gestion_biblioteca.Retiros;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Retiros implements Serializable {
    @SerializedName("ID")
    private Integer id;

    @SerializedName("FECHA")
    private String fecha;

    @SerializedName("CODIGO")
    private String codigo;

    @SerializedName("LIBRO")
    private String libro;

    @SerializedName("NOMBRE")
    private String nombre;

    @SerializedName("TELEFONO")
    private String telefono;

    public Retiros() {}

    // Constructor, getters y setters
    public Retiros(Integer id,String fecha, String codigo, String libro, String nombre, String telefono) {
        this.id = id;
        this.fecha = fecha;
        this.codigo = codigo;
        this.libro = libro;
        this.nombre = nombre;
        this.telefono = telefono;
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

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

}
