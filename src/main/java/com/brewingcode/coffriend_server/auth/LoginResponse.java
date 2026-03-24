package com.brewingcode.coffriend_server.auth;

/**
 * Resposta del servidor a una petició d'autenticació.
 * Conté l'estat de l'operació, un missatge informatiu i el token d'accés.
 */
public class LoginResponse {
    private boolean success;
    private String message;
    private String token;

    /**
     * Constructor per crear una resposta de login.
     * @param success Indica si l'accés ha estat permès.
     * @param message Text descriptiu del resultat.
     * @param token Identificador de sessió generat (null si falla).
     */
    public LoginResponse(boolean success, String message, String token) {
        this.success = success;
        this.message = message;
        this.token = token;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }
}
