package com.example.TomyPaz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@RestController
public class TestController {

    @Autowired
    private EntityManager entityManager;

    @GetMapping("/test-connection")
    public String testConnection() {
        try {
            Query query = entityManager.createNativeQuery("SELECT 1");
            query.getSingleResult();
            return "Conexión exitosa!";
        } catch (Exception e) {
            return "Error en la conexión: " + e.getMessage();
        }
    }
}

