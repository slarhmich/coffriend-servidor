package com.brewingcode.coffriend_servidor.auth;

/**
 * Objecte per sol·licitar el tancament de sessió.
 * Requereix el token actiu que es vol invalidar.
 */
public class LogoutRequest {
    private String token;

    public LogoutRequest() {
    }

    /**
     * Constructor con token
     * @param token El token actiu de l'usuari
     */
    public LogoutRequest(String token) {
        this.token = token;
    }

    /** @return El token que s'enviarà per tancar la sessió. */
    public String getToken() {
        return token;
    }

    /** @param token El token actiu de l'usuari. */
    public void setToken(String token) {
        this.token = token;
    }
}
