package com.brewingcode.coffriend_servidor.controllers;

import com.brewingcode.coffriend_servidor.dto.LoginDTO;
import com.brewingcode.coffriend_servidor.dto.UsuariDTO;
import com.brewingcode.coffriend_servidor.repositories.UsuariRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Handles authentication of the user.
 * @param usuariRepository
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuariRepository usuariRepository;

    @Autowired
    private com.brewingcode.coffriend_servidor.security.JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<com.brewingcode.coffriend_servidor.dto.AuthResponseDTO> login(@RequestBody LoginDTO dto) {
        return usuariRepository.findByEmail(dto.getEmail())
                .filter(usuari -> usuari.getPassword().equals(dto.getPassword()))
                .map(usuari -> {
                    UsuariDTO userDto = new UsuariDTO(usuari.getId(), usuari.getNom(), usuari.getEmail(), 
                            usuari.getRol(), usuari.getNivell(), usuari.getPunts(), 
                            usuari.getBotiga() != null ? usuari.getBotiga().getId() : null);
                    
                    String token = jwtService.generateToken(usuari.getId(), usuari.getRol());
                    return ResponseEntity.ok(new com.brewingcode.coffriend_servidor.dto.AuthResponseDTO(token, userDto));
                })
                .orElse(ResponseEntity.status(401).build());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }
}
