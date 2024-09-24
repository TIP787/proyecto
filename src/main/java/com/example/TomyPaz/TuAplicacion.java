package com.example.TomyPaz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = { /* todas las clases de auto-configuración */ })
public class TuAplicacion {

    public static void main(String[] args) {
        SpringApplication.run(TuAplicacion.class, args);
    }
}

