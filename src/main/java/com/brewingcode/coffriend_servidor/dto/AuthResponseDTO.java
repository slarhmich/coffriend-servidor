package com.brewingcode.coffriend_servidor.dto;

public class AuthResponseDTO {
    private String token;
    private UsuariDTO user;

    public AuthResponseDTO() {}

    public AuthResponseDTO(String token, UsuariDTO user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UsuariDTO getUser() {
        return user;
    }

    public void setUser(UsuariDTO user) {
        this.user = user;
    }
}
