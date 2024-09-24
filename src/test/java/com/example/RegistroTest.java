package com.example;

import com.example.Paz.Registro;
import com.example.Respuesta.UsuarioResponse;
import com.example.modelo.Usuarios;
import com.example.repositorios.Base_de_datos;
import com.example.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class RegistroTest {

    @InjectMocks
    private Registro registro;

    @Mock
    private Base_de_datos base_de_datos;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegistrarUsuarioConExito() {
        Usuarios usuario = new Usuarios();
        usuario.setEmail("test@example.com");
        usuario.setPassword("Password12"); // Contraseña válida según la expresión regular
        usuario.setName("Test User");

        // Mocking del comportamiento de la base de datos y el password encoder
        when(base_de_datos.existsByEmail(usuario.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(usuario.getPassword())).thenReturn("encodedPassword");
        when(jwtUtil.generateToken(usuario.getEmail())).thenReturn("mockToken");
        when(base_de_datos.save(usuario)).thenReturn(usuario);

        Usuarios result = registro.registrarUsuario(usuario);

        assertNotNull(result);
        assertEquals("mockToken", result.getToken());
        assertEquals("encodedPassword", result.getPassword());
    }

    @Test
    public void testRegistrarUsuarioConEmailDuplicado() {
        Usuarios usuario = new Usuarios();
        usuario.setEmail("test@example.com");
        usuario.setPassword("Password12"); // Contraseña válida según la expresión regular

        when(base_de_datos.existsByEmail(usuario.getEmail())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            registro.registrarUsuario(usuario);
        });

        assertEquals("El correo ya está en uso.", exception.getMessage());
    }

    @Test
    public void testVerificarContraseña() {
        String rawPassword = "Password12";
        String encodedPassword = "encodedPassword";

        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        boolean isPasswordValid = registro.verificarContraseña(rawPassword, encodedPassword);

        assertTrue(isPasswordValid);
    }

    @Test
    public void testBuscarUsuarioPorId() {
        Long userId = 1L;
        Usuarios usuario = new Usuarios();
        usuario.setId(userId);

        when(base_de_datos.findById(userId)).thenReturn(Optional.of(usuario));

        Optional<Usuarios> result = registro.buscarUsuarioPorId(userId);

        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
    }

    @Test
    public void testBuscarUsuarioPorEmail() {
        String email = "test@example.com";
        Usuarios usuario = new Usuarios();
        usuario.setEmail(email);

        when(base_de_datos.findByEmail(email)).thenReturn(Optional.of(usuario));

        Optional<Usuarios> result = registro.buscarUsuarioPorEmail(email);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }
}
