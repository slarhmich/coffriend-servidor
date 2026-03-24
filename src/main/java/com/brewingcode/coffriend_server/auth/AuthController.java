package com.brewingcode.coffriend_server.auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST per a la gestió de l'autenticació d'usuaris.
 * Proporciona els punts d'accés per a l'inici i tancament de sessió.
 */
@RestController
public class AuthController {
    String token = "";

    /**
     * Valida les credencials de l'usuari i genera un token d'accés.
     * * @param request LoginRequest amb el nom d'usuari i la contrasenya.
     * @return LoginResponse amb l'estat de l'operació i el token si és correcte.
     */
    @PostMapping("/api/login")
    public LoginResponse loginMock(@RequestBody LoginRequest request) {

        if ("admin".equals(request.getUsername()) && "1234".equals(request.getPassword())) {
            token = "fake-jwt-token-999";
            return new LoginResponse(true, "Login exitoso", "fake-jwt-token-999");
        } else {
            return new LoginResponse(false, "Usuario o contraseña incorrectos", null);
        }
    }

    /**
     * Finalitza la sessió activa invalidant el token proporcionat.
     * * @param request LogoutRequest que conté el token a tancar.
     * @return LogoutResponse indicant si la sessió s'ha tancat correctament.
     */
    @PostMapping("/api/logout")
    public LogoutResponse logoutMock(@RequestBody LogoutRequest request) {
        if (token.equals(request.getToken())) {
            token = "";
            return new LogoutResponse(true, "Sesión cerrada correctamente.");
        } else {
            return new LogoutResponse(false, "Error: Token inválido o sesión ya cerrada.");
        }
    }
}