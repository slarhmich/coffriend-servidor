package com.brewingcode.coffriend_server.auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    // Login
    @PostMapping("/api/login")
    public LoginResponse loginMock(@RequestBody LoginRequest request) {

        if ("admin".equals(request.getUsername()) && "1234".equals(request.getPassword())) {
            return new LoginResponse(true, "Login exitoso", "fake-jwt-token-999");
        } else {
            return new LoginResponse(false, "Usuario o contraseña incorrectos", null);
        }
    }

    // Logout
    @PostMapping("/api/logout")
    public LogoutResponse logoutMock(@RequestBody LogoutRequest request) {

        if ("fake-jwt-token-999".equals(request.getToken())) {
            return new LogoutResponse(true, "Sesión cerrada correctamente.");
        } else {
            return new LogoutResponse(false, "Error: Token inválido o sesión ya cerrada.");
        }
    }
}