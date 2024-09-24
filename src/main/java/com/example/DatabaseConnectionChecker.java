package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class DatabaseConnectionChecker implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void run(String... args) throws Exception {
        if (entityManager != null) {
            System.out.println("Conexión con la base de datos exitosa.");
        } else {
            System.out.println("Error al conectar con la base de datos.");
        }
    }
}