package com.example.Paz;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

import com.example.repositorios.Base_de_datos;
import com.example.util.JwtUtil;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.modelo.Usuarios;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Registro {

    @Autowired
    private Base_de_datos base_de_datos;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;  // Autowire del encoder

    private static final Logger logger = LoggerFactory.getLogger(Registro.class);

    // Expresiones regulares
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d{2})(?!.*\\d{3,})[a-zA-Z\\d]{8,12}$";

    @Transactional
    public Usuarios registrarUsuario(Usuarios usuario) {
        // Verificar si el correo ya está en uso
        if (base_de_datos.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El correo ya está en uso.");
        }

        // Validar email
        if (!Pattern.matches(EMAIL_REGEX, usuario.getEmail())) {
            throw new IllegalArgumentException("Formato de correo inválido");
        }

        // Validar contraseña
        if (!Pattern.matches(PASSWORD_REGEX, usuario.getPassword())) {
            throw new IllegalArgumentException("Contraseña inválida: debe tener una mayúscula, dos números y entre 8 y 12 caracteres.");
        }

        // Encriptar la contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        usuario.setCreated(LocalDateTime.now());
        usuario.setLastLogin(LocalDateTime.now());
        usuario.setIsActive(true);

        // Generar el token JWT
        String token = jwtUtil.generateToken(usuario.getEmail());
        usuario.setToken(token);

        // Guardar el usuario en la base de datos
        return base_de_datos.save(usuario);
    }

    public boolean verificarContraseña(String rawPassword, String encryptedPassword) {
        if (rawPassword == null || rawPassword.isEmpty() || encryptedPassword == null || encryptedPassword.isEmpty()) {
            throw new IllegalArgumentException("Las contraseñas no deben estar vacías.");
        }
        return passwordEncoder.matches(rawPassword, encryptedPassword);
    }

    public Optional<Usuarios> buscarUsuarioPorId(Long id) {
        return base_de_datos.findById(id);
    }

    public Optional<Usuarios> buscarUsuarioPorEmail(String email) {
        return base_de_datos.findByEmail(email);
    }

    @Transactional
    public Usuarios actualizarUsuario(Usuarios usuario) {
        return base_de_datos.save(usuario); // Este metodo actualiza el usuario si ya existe
    }

    // Metodo para probar la conexión
    public void probarConexion() {
        logger.info("Probando la conexión con la base de datos...");
        Usuarios usuario = new Usuarios(null, "Test User", "test@example.com", "Password123", null, 0, null);
        usuario.setIsActive(true);
        usuario.setCreated(LocalDateTime.now());
        usuario.setLastLogin(LocalDateTime.now());

        // Guardar el usuario en la base de datos
        base_de_datos.save(usuario);
        logger.info("Usuario de prueba guardado correctamente.");
    }

    @PostConstruct
    public void init() {
        if (!base_de_datos.existsByEmail("test@example.com")) {
            probarConexion();
        }
    }
}
