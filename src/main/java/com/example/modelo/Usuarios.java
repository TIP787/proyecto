package com.example.modelo;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import java.util.List;


@Entity
public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = true)
    private String name;
    private String email;
    private String password;
    private Long number;
    private int cityCode;
    private String countrycode;
    private LocalDateTime created;
    private LocalDateTime lastLogin;
    private boolean isActive;
    private String token; 

    @OneToMany(cascade = CascadeType.ALL)
    private List<Telefono> phones;


    // Constructor vacío
    public Usuarios() {
    }

    
    // Constructor
    public Usuarios(Long id, String name, String email, String password, Long number, int cityCode, String countrycode) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.number = number;
        this.cityCode = cityCode;
        this.countrycode = countrycode;
    }


    
    




    // Getters y Setters
    public List<Telefono> getPhones() {
        return phones;
    }

    public void setPhones(List<Telefono> phones) {
        this.phones = phones;

    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }


    public LocalDateTime getCreated() {
        return created;
    }
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }
    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Long getNumber() {
        return number;
    }
    public void setNumber(Long number) {
        this.number = number;
    }

    public int getCityCode() {
        return cityCode;
    }
    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public String getCountryCode() {
        return countrycode;
    }
    public void setCountryCode(String countrycode) {
        this.countrycode = countrycode;
    }
}
