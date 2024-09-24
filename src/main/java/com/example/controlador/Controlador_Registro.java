package com.example.controlador;

import com.example.modelo.Usuarios;
import com.example.Paz.Registro;
import com.example.Respuesta.UsuarioResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class Controlador_Registro {

    @Autowired
    private Registro registro;

    @PostMapping("/sign-up")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuarios usuario) {
        try {
            // Validar que el usuario tiene al menos un teléfono
        Usuarios nuevoUsuario = registro.registrarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerUsuario(@PathVariable Long id) {
        return registro.buscarUsuarioPorId(id)
            .map(usuario -> ResponseEntity.ok().body(usuario))
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
}
