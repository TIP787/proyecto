package com.example.controlador;

import com.example.Respuesta.UsuarioResponse;
import com.example.modelo.Usuarios;
import com.example.repositorios.Base_de_datos;
import com.example.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.example.modelo.Telefono;


@RestController
@RequestMapping("/api")
public class UsuarioController {

    @Autowired
    private Base_de_datos repositorio;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/login")
public ResponseEntity<UsuarioResponse> login(@RequestHeader("Authorization") String token) {
    // Extraer el email del token
    String email = jwtUtil.extractEmail(token);

    // Buscar el usuario en la base de datos
    Optional<Usuarios> optionalUsuario = repositorio.findByEmail(email);
    
    if (optionalUsuario.isEmpty()) {
        return ResponseEntity.status(404).build(); // Usuario no encontrado
    }

    Usuarios usuario = optionalUsuario.get(); // Extraer el usuario del Optional

    // Actualizar el token
    String newToken = jwtUtil.generateToken(email);
    
    // Crear la respuesta
    UsuarioResponse response = new UsuarioResponse();
    response.setId(usuario.getId());
    response.setCreated(usuario.getCreated());
    response.setLastLogin(LocalDateTime.now()); // Aquí podrías tener lógica para obtener la última conexión
    response.setToken(newToken);
    response.setIsActive(true); // Establecer si el usuario está activo
    response.setName(usuario.getName());
    response.setEmail(usuario.getEmail());
    response.setPassword(usuario.getPassword()); // Considera no exponer la contraseña
    response.setPhones(List.of(new Telefono(usuario.getNumber(), usuario.getCityCode(), usuario.getCountryCode())));

    return ResponseEntity.ok(response);
}
}
