package com.brewingcode.coffriend_servidor.controllers;

import com.brewingcode.coffriend_servidor.dto.UsuariDTO;
import com.brewingcode.coffriend_servidor.entities.Usuari;
import com.brewingcode.coffriend_servidor.repositories.UsuariRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles CRUD of user.
 * @param usuariRepository
 */
@RestController
@RequestMapping("/api/usuaris")
public class UsuariController {

    @Autowired
    private UsuariRepository usuariRepository;

    // CREATE
    @PostMapping
    public ResponseEntity<UsuariDTO> create(@RequestBody UsuariDTO dto, org.springframework.security.core.Authentication auth) {
        if (usuariRepository.existsByEmail(dto.getEmail())) {
            return ResponseEntity.badRequest().build();
        }
        
        boolean isAuthenticated = auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser");
        
        if (usuariRepository.count() == 0) {
            // First User Claim: If database is completely empty, make the first user an admin.
            dto.setRol("admin");
        } else if (isAuthenticated) {
            boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_admin"));
            if (!isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            dto.setRol(dto.getRol() != null ? dto.getRol() : "client");
        } else {
            dto.setRol("client");
        }

        Usuari usuari = new Usuari();
        usuari.setNom(dto.getNom());
        usuari.setEmail(dto.getEmail());
        usuari.setContrasenya(dto.getContrasenya() != null ? dto.getContrasenya() : dto.getEmail());
        usuari.setRol(dto.getRol());
        
        if ("client".equals(usuari.getRol())) {
            usuari.setNivell(dto.getNivell() != null ? dto.getNivell() : 1);
            usuari.setPunts(dto.getPunts() != null ? dto.getPunts() : 0);
        }
        
        Usuari saved = usuariRepository.save(usuari);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<UsuariDTO>> getAll() {
        List<UsuariDTO> usuaris = usuariRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuaris);
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuariDTO> getById(@PathVariable Integer id) {
        return usuariRepository.findById(id)
                .map(u -> ResponseEntity.ok(toDTO(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<UsuariDTO> update(@PathVariable Integer id, @RequestBody UsuariDTO dto, org.springframework.security.core.Authentication auth) {
        return usuariRepository.findById(id).map(usuari -> {
            boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_admin"));
            Integer callerId = (Integer) auth.getPrincipal();
            
            if (isAdmin) {
                if (!usuari.getId().equals(callerId) && "client".equals(usuari.getRol())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).<UsuariDTO>build();
                }
            } else {
                if (!usuari.getId().equals(callerId)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).<UsuariDTO>build();
                }
            }

            usuari.setNom(dto.getNom());
            usuari.setEmail(dto.getEmail());
            usuari.setRol(dto.getRol());
            
            if ("client".equals(usuari.getRol())) {
                usuari.setNivell(dto.getNivell());
                usuari.setPunts(dto.getPunts());
            }
            
            Usuari updated = usuariRepository.save(usuari);
            return ResponseEntity.ok(toDTO(updated));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id, org.springframework.security.core.Authentication auth) {
        return usuariRepository.findById(id).map(usuari -> {
            boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_admin"));
            boolean isClient = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_client"));
            Integer callerId = (Integer) auth.getPrincipal();

            if (isAdmin) {
                 // Admin can delete
            } else if (isClient) {
                 if (!usuari.getId().equals(callerId)) {
                     return ResponseEntity.status(HttpStatus.FORBIDDEN).<Void>build();
                 }
            } else {
                 // Worker cannot delete
                 return ResponseEntity.status(HttpStatus.FORBIDDEN).<Void>build();
            }

            usuariRepository.deleteById(id);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    private UsuariDTO toDTO(Usuari u) {
        return new UsuariDTO(u.getId(), u.getNom(), u.getEmail(), u.getRol(), 
                u.getNivell(), u.getPunts(), u.getBotiga() != null ? u.getBotiga().getId() : null);
    }
}