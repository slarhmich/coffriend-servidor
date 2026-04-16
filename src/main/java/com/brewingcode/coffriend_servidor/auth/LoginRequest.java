package com.brewingcode.coffriend_servidor.auth;

/**
 * Objecte per sol·licitar el login.
 * Requereix el nom d'usuari i la contrasenya de l'usuari que es vol autenticar.
 */
public class LoginRequest {
    private String username;
    private String password;

    /** @return El nom d'usuari enviat. */
    public String getUsername() {
        return username;
    }

    /** @param username El nom d'usuari a establir. */
    public void setUsername(String username) {
        this.username = username;
    }

    /** @return La contrasenya enviada. */
    public String getPassword() {
        return password;
    }

    /** @param password La contrasenya a establir. */
    public void setPassword(String password) {
        this.password = password;
    }
}