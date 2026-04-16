package com.brewingcode.coffriend_servidor.controllers;

import com.brewingcode.coffriend_servidor.dto.ComandaDTO;
import com.brewingcode.coffriend_servidor.dto.LiniaComandaDTO;
import com.brewingcode.coffriend_servidor.entities.Comanda;
import com.brewingcode.coffriend_servidor.entities.LiniaComanda;
import com.brewingcode.coffriend_servidor.repositories.ComandaRepository;
import com.brewingcode.coffriend_servidor.repositories.LiniaComandaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles CRUD of comanda.
 * @param botigaRepository
 * @param comandaRepository
 * @param liniaComandaRepository
 * @param usuariRepository
 * @param producteRepository
 */
@RestController
@RequestMapping("/api/comandes")
public class ComandaController {

    @Autowired
    private ComandaRepository comandaRepository;

    @Autowired
    private LiniaComandaRepository liniaComandaRepository;

    @Autowired
    private com.brewingcode.coffriend_servidor.repositories.UsuariRepository usuariRepository;

    @Autowired
    private com.brewingcode.coffriend_servidor.repositories.BotigaRepository botigaRepository;

    @Autowired
    private com.brewingcode.coffriend_servidor.repositories.ProducteRepository producteRepository;

    // GET ALL
    @GetMapping
    public ResponseEntity<List<ComandaDTO>> getAll() {
        List<ComandaDTO> comandes = comandaRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(comandes);
    }

    // GET BY USER (CLIENT)
    @GetMapping("/usuario/{idUsuari}")
    public ResponseEntity<List<ComandaDTO>> getByUsuari(@PathVariable Integer idUsuari) {
        List<ComandaDTO> comandes = comandaRepository.findByIdUsuari(idUsuari)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(comandes);
    }

    // GET BY BOTIGA (WORKER)
    @GetMapping("/botiga/{idBotiga}")
    public ResponseEntity<List<ComandaDTO>> getByBotiga(@PathVariable Integer idBotiga) {
        List<ComandaDTO> comandes = comandaRepository.findByIdBotiga(idBotiga)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(comandes);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ComandaDTO> getById(@PathVariable Integer id) {
        return comandaRepository.findById(id)
                .map(c -> ResponseEntity.ok(toDTO(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    // CREATE (CLIENT)
    @PreAuthorize("hasAuthority('ROLE_client')")
    @PostMapping
    public ResponseEntity<ComandaDTO> create(@RequestBody ComandaDTO dto, Authentication auth) {
        // Auto-resolve user from JWT token
        Integer userId = Integer.parseInt(auth.getName());
        var usuari = usuariRepository.findById(userId);
        if (usuari.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        if (dto.getIdBotiga() == null) {
            return ResponseEntity.badRequest().build();
        }
        var botiga = botigaRepository.findById(dto.getIdBotiga());
        if (botiga.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Comanda comanda = new Comanda();
        comanda.setDataHora(LocalDateTime.now());
        comanda.setEstat("pendent");
        comanda.setTipus(dto.getTipus());
        comanda.setUsuari(usuari.get());
        comanda.setBotiga(botiga.get());
        
        Comanda saved = comandaRepository.save(comanda);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
    }

    // UPDATE STATUS (WORKER or ADMIN)
    @PreAuthorize("hasRole('admin') or hasRole('treballador')")
    @PutMapping("/{id}/estat/{estat}")
    public ResponseEntity<ComandaDTO> updateEstat(@PathVariable Integer id, @PathVariable String estat) {
        return comandaRepository.findById(id).map(comanda -> {
            comanda.setEstat(estat);
            Comanda updated = comandaRepository.save(comanda);
            return ResponseEntity.ok(toDTO(updated));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id, Authentication auth) {
        return comandaRepository.findById(id).map(comanda -> {
            boolean isClient = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_client"));
            Integer callerId = (Integer) auth.getPrincipal();
            if (isClient && (comanda.getUsuari() == null || !comanda.getUsuari().getId().equals(callerId))) {
                 return ResponseEntity.status(HttpStatus.FORBIDDEN).<Void>build();
            }
            comandaRepository.deleteById(id);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // ADD LINE (CLIENT)
    @PreAuthorize("hasRole('client')")
    @PostMapping("/{id}/linies")
    public ResponseEntity<LiniaComandaDTO> addLinea(@PathVariable Integer id, @RequestBody LiniaComandaDTO dto) {
        return comandaRepository.findById(id).map(comanda -> {
            LiniaComanda linea = new LiniaComanda();
            linea.setQuantitat(dto.getQuantitat());
            linea.setPreuUnitari(java.math.BigDecimal.valueOf(dto.getPreuUnitari()));
            
            if (dto.getIdProducte() != null) {
                producteRepository.findById(dto.getIdProducte()).ifPresent(linea::setProducte);
            }
            linea.setComanda(comanda);
            
            LiniaComanda saved = liniaComandaRepository.save(linea);
            return ResponseEntity.status(HttpStatus.CREATED).body(toLiniaDTO(saved));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE LINE (CLIENT)
    @PreAuthorize("hasRole('client')")
    @DeleteMapping("/linies/{idComanda}/{idProducte}")
    public ResponseEntity<Void> deleteLinea(@PathVariable Integer idComanda, @PathVariable Integer idProducte) {
        liniaComandaRepository.deleteById(new com.brewingcode.coffriend_servidor.entities.LiniaComandaId(idComanda, idProducte));
        return ResponseEntity.noContent().build();
    }

    private ComandaDTO toDTO(Comanda c) {
        List<LiniaComandaDTO> linies = liniaComandaRepository.findByComandaId(c.getId())
                .stream()
                .map(this::toLiniaDTO)
                .collect(Collectors.toList());
        
        return new ComandaDTO(c.getId(), c.getDataHora(), c.getEstat(), c.getTipus(),
                c.getUsuari() != null ? c.getUsuari().getId() : null,
                c.getBotiga() != null ? c.getBotiga().getId() : null, linies);
    }

    private LiniaComandaDTO toLiniaDTO(LiniaComanda l) {
        return new LiniaComandaDTO(l.getComanda().getId(), l.getProducte().getId(),
                l.getQuantitat(), l.getPreuUnitari().doubleValue());
    }
}
