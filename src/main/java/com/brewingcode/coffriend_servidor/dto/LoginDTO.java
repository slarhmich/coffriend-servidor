package com.brewingcode.coffriend_servidor.dto;

public class LoginDTO {
    private String email;
    private String contrasenya;
    
    public LoginDTO() {}
    public LoginDTO(String email, String contrasenya) {
        this.email = email;
        this.contrasenya = contrasenya;
    }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getContrasenya() { return contrasenya; }
    public void setContrasenya(String contrasenya) { this.contrasenya = contrasenya; }
}
