package com.example.controlador;

import com.example.modelo.Usuarios;
import com.example.Paz.Registro;
import com.example.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class Controlador_Login {


    @Autowired
    private Registro registro;  // Servicio para interactuar con la base de datos

    @Autowired
    private JwtUtil jwtUtil;  // Utilidad para gestionar el JWT

    @PostMapping
    public ResponseEntity<?> login(@RequestHeader("Authorization") String token) {
        try {
            // Extraer el email del token
            String email = jwtUtil.extractEmail(token);

            // Buscar el usuario en la base de datos
            Usuarios usuario = registro.buscarUsuarioPorEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            // Generar un nuevo token
            String nuevoToken = jwtUtil.generateToken(email);

            // Actualizar el token en el usuario
            usuario.setToken(nuevoToken);

            // Actualizar la fecha de último inicio de sesión
            usuario.setLastLogin(java.time.LocalDateTime.now());

            // Guardar los cambios en la base de datos
            registro.actualizarUsuario(usuario);

            // Devolver la información del usuario y el nuevo token
            return ResponseEntity.ok().body(usuario);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o expirado");
        }
    }
}

