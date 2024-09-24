package com.example.modelo;

import jakarta.persistence.*;

@Entity
public class Telefono {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Long number;
    private int citycode;
    private String countrycode;


    public Telefono(Long number, int cityCode, String countryCode) {
        this.number = number;
        this.citycode = cityCode;
        this.countrycode = countryCode;
    }

    public Telefono() {

    }

    // Constructor y Getters/Setters...

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public int getCitycode() {
        return citycode;
    }

    public void setCitycode(int citycode) {
        this.citycode = citycode;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }
}
