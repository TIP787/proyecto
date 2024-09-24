package com.example.repositorios;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.modelo.Usuarios;

public interface Base_de_datos extends JpaRepository<Usuarios, Long> {
    Optional<Usuarios> findByEmail(String email);
    boolean existsByEmail(String email);
}



