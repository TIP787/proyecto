package com.example.gestion_biblioteca.Socios;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Socios implements Serializable {
    @SerializedName("ID")
    private Integer id;

    @SerializedName("Nombre")
    private String nombre;

    @SerializedName("DNI")
    private String dni;

    @SerializedName("Direccion")
    private String direccion;

    @SerializedName("Telefono")
    private String telefono;

    @SerializedName("Correo")
    private String correo;

    @SerializedName("Fecha")
    private String fecha;

    // Constructor vacío
    public Socios() {}

    // Constructor completo
    public Socios(Integer id, String nombre, String dni, String direccion,String telefono, String correo, String fecha) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.fecha = fecha;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }


    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}
