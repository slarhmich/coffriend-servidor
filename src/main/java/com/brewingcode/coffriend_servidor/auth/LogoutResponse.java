package com.brewingcode.coffriend_servidor.auth;

/**
 * Resposta del servidor després d'intentar tancar una sessió.
 */
public class LogoutResponse {
    private boolean success;
    private String message;

    /**
     * Constructor per a la resposta de tancament de sessió.
     * @param success Indica si la sessió s'ha tancat correctament.
     * @param message Detalls del resultat de l'operació.
     */
    public LogoutResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /** @return Estat de l'operació de logout. */
    public boolean isSuccess() {
        return success;
    }

    /** @return Missatge de confirmació o error. */
    public String getMessage() {
        return message;
    }
}