package com.brewingcode.coffriend_servidor.dto;

public class AuthResponseDTO {
    private String token;
    private UsuariDTO usuari;

    public AuthResponseDTO() {}

    public AuthResponseDTO(String token, UsuariDTO usuari) {
        this.token = token;
        this.usuari = usuari;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UsuariDTO getUsuari() {
        return usuari;
    }

    public void setUsuari(UsuariDTO usuari) {
        this.usuari = usuari;
    }
}
