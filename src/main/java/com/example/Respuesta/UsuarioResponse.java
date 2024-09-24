package com.example.Respuesta;

import java.time.LocalDateTime;
import java.util.List;

import com.example.modelo.Telefono;

public class UsuarioResponse {
    private Long id;
    private LocalDateTime created;
    private LocalDateTime lastLogin;
    private String token;
    private boolean isActive;
    private String name;
    private String email;
    private List<Telefono> phones;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long long1) { this.id = long1; }
    public LocalDateTime getCreated() { return created; }
    public void setCreated(LocalDateTime created) { this.created = created; }
    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public boolean isActive() { return isActive; }
    public void setIsActive(boolean isActive) { this.isActive = isActive; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<Telefono> getPhones() { return phones; }
    public void setPhones(List<Telefono> phones) { this.phones = phones; }
    public void setPassword(String password) {
        
        throw new UnsupportedOperationException("Unimplemented method 'setPassword'");
    }
    public Object getPassword() {
        throw new UnsupportedOperationException("Unimplemented method 'getPassword'");
    }
}
