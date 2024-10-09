package com.example.gestion_biblioteca.Categorias;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Categorias implements Serializable {
    @SerializedName("ID")
    private Integer id;

    @SerializedName("CATEGORIA")
    private String categoria;

    public Categorias(Integer id,String categoria) {
        this.id = id;
        this.categoria = categoria;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}