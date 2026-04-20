package com.brewingcode.coffriend_servidor.controllers;

import com.brewingcode.coffriend_servidor.dto.EarnedInsigniaDTO;
import com.brewingcode.coffriend_servidor.dto.UsuariDTO;
import com.brewingcode.coffriend_servidor.dto.UsuariPublicDTO;
import com.brewingcode.coffriend_servidor.entities.Usuari;
import com.brewingcode.coffriend_servidor.repositories.UsuariInsigniaRepository;
import com.brewingcode.coffriend_servidor.repositories.UsuariRepository;
import com.brewingcode.coffriend_servidor.security.AuthorizationService;
import com.brewingcode.coffriend_servidor.security.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private UsuariInsigniaRepository usuariInsigniaRepository;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // CREATE
    @PostMapping
    public ResponseEntity<UsuariDTO> create(@RequestBody UsuariDTO dto, org.springframework.security.core.Authentication auth) {
        if (usuariRepository.existsByEmail(dto.getEmail())) {
            return ResponseEntity.badRequest().build();
        }
        
        boolean isAuthenticated = authorizationService.isAuthenticated(auth);
        
        // s'adjudica administrador al primer usuari enregistrat. 
        // si l'usuari no es administrador el rol ha de ser client
        if (usuariRepository.count() == 0) {
            dto.setRol(RoleEnum.ADMIN.getDbValue());
        } else if (isAuthenticated) {
            if (!authorizationService.hasRole(auth, RoleEnum.ADMIN)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            dto.setRol(dto.getRol() != null ? dto.getRol() : RoleEnum.CLIENT.getDbValue());
        } else {
            dto.setRol(RoleEnum.CLIENT.getDbValue());
        }

        Usuari usuari = new Usuari();
        usuari.setNom(dto.getNom());
        usuari.setEmail(dto.getEmail());
        usuari.setPassword(passwordEncoder.encode(dto.getPassword() != null ? dto.getPassword() : dto.getEmail()));
        usuari.setRol(dto.getRol());
        
        if (RoleEnum.CLIENT.getDbValue().equals(usuari.getRol())) {
            usuari.setNivell(dto.getNivell() != null ? dto.getNivell() : 1);
            usuari.setPunts(dto.getPunts() != null ? dto.getPunts() : 0);
        }
        
        Usuari saved = usuariRepository.save(usuari);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<UsuariDTO>> getAll(org.springframework.security.core.Authentication auth) {
      if (!authorizationService.hasRole(auth, RoleEnum.ADMIN)) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }
      List<UsuariDTO> usuaris = usuariRepository.findAll()
          .stream()
          .map(this::toDTO)
          .collect(Collectors.toList());
      return ResponseEntity.ok(usuaris);
    }

    // READ BY ID
    // - ADMIN: full profile of any user
    // - STAFF: public view of any user
    // - CLIENT/self: full own profile
    // - Anyone else: 403
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id, org.springframework.security.core.Authentication auth) {
        if (!authorizationService.isAuthenticated(auth)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return usuariRepository.findById(id).map(u -> {
            if (authorizationService.hasRole(auth, RoleEnum.ADMIN)) {
                return (ResponseEntity<?>) ResponseEntity.ok(toDTO(u));
            }
            if (authorizationService.hasRole(auth, RoleEnum.STAFF)) {
                return (ResponseEntity<?>) ResponseEntity.ok(toPublicDTO(u));
            }
            if (authorizationService.canManageUser(auth, id)) {
                return (ResponseEntity<?>) ResponseEntity.ok(toDTO(u));
            }
            return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<UsuariDTO> update(@PathVariable Integer id, @RequestBody UsuariDTO dto, org.springframework.security.core.Authentication auth) {
        return usuariRepository.findById(id).map(usuari -> {
            if (!authorizationService.canManageUser(auth, id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).<UsuariDTO>build();
            }

            usuari.setNom(dto.getNom());
            usuari.setEmail(dto.getEmail());

            if (authorizationService.hasRole(auth, RoleEnum.ADMIN)) {
                usuari.setRol(dto.getRol());
            }
            
            if (RoleEnum.CLIENT.getDbValue().equals(usuari.getRol())) {
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
            boolean isAdmin = authorizationService.hasRole(auth, RoleEnum.ADMIN);
            boolean isClient = authorizationService.hasRole(auth, RoleEnum.CLIENT);

            // only admin can delete
            if (isAdmin) {
            } else if (isClient) {
                 if (!authorizationService.canManageUser(auth, id)) {
                     return ResponseEntity.status(HttpStatus.FORBIDDEN).<Void>build();
                 }
            } else {
                 return ResponseEntity.status(HttpStatus.FORBIDDEN).<Void>build();
            }

            usuariRepository.deleteById(id);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    private UsuariDTO toDTO(Usuari u) {
        UsuariDTO dto = new UsuariDTO(u.getId(), u.getNom(), u.getEmail(), u.getRol(),
                u.getNivell(), u.getPunts(), u.getBotiga() != null ? u.getBotiga().getId() : null);
        dto.setInsignies(buildInsignies(u.getId()));
        return dto;
    }

    private UsuariPublicDTO toPublicDTO(Usuari u) {
        UsuariPublicDTO dto = new UsuariPublicDTO(u.getId(), u.getNom(), u.getNivell(), u.getPunts());
        dto.setInsignies(buildInsignies(u.getId()));
        return dto;
    }

    private List<EarnedInsigniaDTO> buildInsignies(Integer usuariId) {
        return usuariInsigniaRepository.findByUsuariId(usuariId)
                .stream()
                .map(ui -> new EarnedInsigniaDTO(
                        ui.getInsignia().getId(),
                        ui.getInsignia().getNom(),
                        ui.getInsignia().getDescripcio(),
                        ui.getInsignia().getImatgeUrl(),
                        ui.getDataObtencio()))
                .collect(Collectors.toList());
    }
}