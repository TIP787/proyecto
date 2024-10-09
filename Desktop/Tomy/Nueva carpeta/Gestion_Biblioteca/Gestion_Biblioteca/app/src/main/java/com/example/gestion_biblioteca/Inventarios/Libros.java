package com.example.gestion_biblioteca.Inventarios;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Libros implements Serializable {
    @SerializedName("ID")
    private Integer id;

    @SerializedName("NOMBRE")
    private String nombre;

    @SerializedName("CODIGO")
    private String codigo;

    @SerializedName("CATEGORIA")
    private String categoria;

    @SerializedName("UBICACION")
    private String ubicacion;


    public Libros() {}

    public Libros(Integer id, String nombre, String codigo, String categoria, String ubicacion) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
    }

    // Getters y setters
    public Integer getId(){
        return id;
    }
    public void setId(Integer id){
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }


    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUbicacion() {
        return ubicacion;
    }
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}

