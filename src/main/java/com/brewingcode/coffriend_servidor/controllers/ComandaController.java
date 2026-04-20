package com.brewingcode.coffriend_servidor.controllers;

import com.brewingcode.coffriend_servidor.dto.ComandaDTO;
import com.brewingcode.coffriend_servidor.dto.LiniaComandaDTO;
import com.brewingcode.coffriend_servidor.entities.Comanda;
import com.brewingcode.coffriend_servidor.entities.LiniaComanda;
import com.brewingcode.coffriend_servidor.repositories.ComandaRepository;
import com.brewingcode.coffriend_servidor.repositories.LiniaComandaRepository;
import com.brewingcode.coffriend_servidor.security.AuthorizationService;
import com.brewingcode.coffriend_servidor.security.RoleEnum;
import com.brewingcode.coffriend_servidor.service.GamificationService;
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

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private GamificationService gamificationService;

    // get all comandes (of all shops)
    @PreAuthorize("hasRole('admin')")
    @GetMapping
    public ResponseEntity<List<ComandaDTO>> getAll() {
        List<ComandaDTO> comandes = comandaRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(comandes);
    }

    // geta customer's comandes
    @GetMapping("/usuario/{idUsuari}")
    public ResponseEntity<List<ComandaDTO>> getByUsuari(@PathVariable Integer idUsuari, Authentication auth) {
      // if admin any user's comandes can be seen
      if (authorizationService.hasRole(auth, RoleEnum.ADMIN)) {
            List<ComandaDTO> comandes = comandaRepository.findByIdUsuari(idUsuari)
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(comandes);
        }
        
        // staff can see a customer's comandes if they're from their shop
        if (authorizationService.hasRole(auth, RoleEnum.STAFF)) {
            Integer staffUserId = authorizationService.getCurrentUserId(auth);
            var staffMember = usuariRepository.findById(staffUserId);
            
            if (staffMember.isEmpty() || staffMember.get().getBotiga() == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            Integer staffBotigaId = staffMember.get().getBotiga().getId();
            List<ComandaDTO> comandes = comandaRepository.findByIdUsuari(idUsuari)
                    .stream()
                    .filter(c -> c.getBotiga() != null && c.getBotiga().getId().equals(staffBotigaId))
                    .map(this::toDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(comandes);
        }
        
        // clients can only see their own comandes
        if (!authorizationService.canManageUser(auth, idUsuari)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        List<ComandaDTO> comandes = comandaRepository.findByIdUsuari(idUsuari)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(comandes);
    }

    // get all comandes by shop id
    @GetMapping("/botiga/{idBotiga}")
    public ResponseEntity<List<ComandaDTO>> getByBotiga(@PathVariable Integer idBotiga, Authentication auth) {
        // admin can see comandes from any botiga
        if (authorizationService.hasRole(auth, RoleEnum.ADMIN)) {
            List<ComandaDTO> comandes = comandaRepository.findByIdBotiga(idBotiga)
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(comandes);
        }
        
        // staff can only see comandes from their own botiga
        if (authorizationService.hasRole(auth, RoleEnum.STAFF)) {
            Integer staffUserId = authorizationService.getCurrentUserId(auth);
            var staffMember = usuariRepository.findById(staffUserId);
            
            if (staffMember.isEmpty() || staffMember.get().getBotiga() == null ||
                !staffMember.get().getBotiga().getId().equals(idBotiga)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            List<ComandaDTO> comandes = comandaRepository.findByIdBotiga(idBotiga)
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(comandes);
        }
        
        // clients don't have access to botiga comandes
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // get by id
    @GetMapping("/{id}")
    public ResponseEntity<ComandaDTO> getById(@PathVariable Integer id, Authentication auth) {
        return comandaRepository.findById(id).map(comanda -> {
            // admin can see any comanda
            if (authorizationService.hasRole(auth, RoleEnum.ADMIN)) {
                return ResponseEntity.ok(toDTO(comanda));
            }
            
            // staff can see comandes from their botiga
            if (authorizationService.hasRole(auth, RoleEnum.STAFF)) {
                Integer staffUserId = authorizationService.getCurrentUserId(auth);
                var staffMember = usuariRepository.findById(staffUserId);
                
                if (staffMember.isPresent() && staffMember.get().getBotiga() != null &&
                    staffMember.get().getBotiga().getId().equals(comanda.getBotiga().getId())) {
                    return ResponseEntity.ok(toDTO(comanda));
                }
                return ResponseEntity.status(HttpStatus.FORBIDDEN).<ComandaDTO>build();
            }
            
            // clients can only see their own comandes
            if (authorizationService.hasRole(auth, RoleEnum.CLIENT) &&
                authorizationService.canManageUser(auth, comanda.getUsuari().getId())) {
                return ResponseEntity.ok(toDTO(comanda));
            }
            
            return ResponseEntity.status(HttpStatus.FORBIDDEN).<ComandaDTO>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // create comanda
    @PreAuthorize("hasRole('client')")
    @PostMapping
    public ResponseEntity<ComandaDTO> create(@RequestBody ComandaDTO dto, Authentication auth) {
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

    // update status
    @PreAuthorize("hasRole('admin') or hasRole('staff')")
    @PutMapping("/{id}/estat/{estat}")
    public ResponseEntity<ComandaDTO> updateEstat(@PathVariable Integer id, @PathVariable String estat) {
        return comandaRepository.findById(id).map(comanda -> {
            String oldEstat = comanda.getEstat();
            comanda.setEstat(estat);
            Comanda updated = comandaRepository.save(comanda);
            
            // check gamification when order complete
            if ("completat".equals(estat) && !("completat".equals(oldEstat))) {
                gamificationService.processOrderCompletion(updated);
            }
            
            return ResponseEntity.ok(toDTO(updated));
        }).orElse(ResponseEntity.notFound().build());
    }

    // delete comanda by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id, Authentication auth) {
        return comandaRepository.findById(id).map(comanda -> {
            boolean isAdmin = authorizationService.hasRole(auth, RoleEnum.ADMIN);
            boolean isOwner = authorizationService.canManageUser(auth,
                    comanda.getUsuari() != null ? comanda.getUsuari().getId() : null);

            if (!isAdmin && !isOwner) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).<Void>build();
            }

            if (!isAdmin && !"pendent".equals(comanda.getEstat())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).<Void>build();
            }

            comandaRepository.deleteById(id);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // add line to comanda
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

    // delete line from comanda
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
