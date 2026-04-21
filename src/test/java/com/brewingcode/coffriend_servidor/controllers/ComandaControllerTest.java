package com.brewingcode.coffriend_servidor.controllers;

import com.brewingcode.coffriend_servidor.dto.ComandaDTO;
import com.brewingcode.coffriend_servidor.dto.LiniaComandaDTO;
import com.brewingcode.coffriend_servidor.entities.Botiga;
import com.brewingcode.coffriend_servidor.entities.Comanda;
import com.brewingcode.coffriend_servidor.entities.LiniaComanda;
import com.brewingcode.coffriend_servidor.entities.LiniaComandaId;
import com.brewingcode.coffriend_servidor.entities.Producte;
import com.brewingcode.coffriend_servidor.entities.Usuari;
import com.brewingcode.coffriend_servidor.repositories.BotigaRepository;
import com.brewingcode.coffriend_servidor.repositories.ComandaRepository;
import com.brewingcode.coffriend_servidor.repositories.LiniaComandaRepository;
import com.brewingcode.coffriend_servidor.repositories.ProducteRepository;
import com.brewingcode.coffriend_servidor.repositories.UsuariRepository;
import com.brewingcode.coffriend_servidor.security.AuthorizationService;
import com.brewingcode.coffriend_servidor.service.GamificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ComandaController Tests")
class ComandaControllerTest {

    @Mock
    private ComandaRepository comandaRepository;

    @Mock
    private LiniaComandaRepository liniaComandaRepository;

    @Mock
    private UsuariRepository usuariRepository;

    @Mock
    private BotigaRepository botigaRepository;

    @Mock
    private ProducteRepository producteRepository;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private GamificationService gamificationService;

    @InjectMocks
    private ComandaController comandaController;

    @Test
    @DisplayName("create rejects comanda without linies")
    void createRejectsComandaWithoutLinies() {
        Authentication auth = new UsernamePasswordAuthenticationToken("1", null, Collections.emptyList());
        ComandaDTO dto = new ComandaDTO();
        dto.setIdBotiga(2);
        dto.setTipus("takeaway");
        dto.setLinies(List.of());

        when(usuariRepository.findById(1)).thenReturn(Optional.of(buildUsuari(1)));
        when(botigaRepository.findById(2)).thenReturn(Optional.of(buildBotiga(2)));

        ResponseEntity<ComandaDTO> response = comandaController.create(dto, auth);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(comandaRepository, never()).save(any());
        verify(liniaComandaRepository, never()).save(any());
    }

    @Test
    @DisplayName("create persists comanda and initial linies")
    void createPersistsComandaAndInitialLinies() {
        Authentication auth = new UsernamePasswordAuthenticationToken("1", null, Collections.emptyList());

        ComandaDTO dto = new ComandaDTO();
        dto.setIdBotiga(2);
        dto.setTipus("takeaway");
        dto.setLinies(List.of(new LiniaComandaDTO(null, 10, 2, 3.5)));

        Usuari usuari = buildUsuari(1);
        Botiga botiga = buildBotiga(2);
        Producte producte = buildProducte(10);

        when(usuariRepository.findById(1)).thenReturn(Optional.of(usuari));
        when(botigaRepository.findById(2)).thenReturn(Optional.of(botiga));
        when(producteRepository.findById(10)).thenReturn(Optional.of(producte));
        when(comandaRepository.save(any(Comanda.class))).thenAnswer(invocation -> {
            Comanda saved = invocation.getArgument(0);
            saved.setId(100);
            return saved;
        });
        when(liniaComandaRepository.save(any(LiniaComanda.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(liniaComandaRepository.findByComandaId(100)).thenReturn(List.of(buildLinia(100, producte.getId(), 2, 3.5)));

        ResponseEntity<ComandaDTO> response = comandaController.create(dto, auth);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(100, response.getBody().getId());
        assertEquals(1, response.getBody().getLinies().size());

        verify(comandaRepository).save(any(Comanda.class));
        verify(liniaComandaRepository).save(any(LiniaComanda.class));
    }

    @Test
    @DisplayName("deleteLinea removes comanda when it becomes empty")
    void deleteLineaRemovesComandaWhenEmpty() {
        Integer idComanda = 101;
        Integer idProducte = 10;

        when(comandaRepository.existsById(idComanda)).thenReturn(true);
        when(liniaComandaRepository.existsById(any(LiniaComandaId.class))).thenReturn(true);
        when(liniaComandaRepository.findByComandaId(idComanda)).thenReturn(List.of());

        ResponseEntity<Void> response = comandaController.deleteLinea(idComanda, idProducte);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(liniaComandaRepository).deleteById(eq(new LiniaComandaId(idComanda, idProducte)));
        verify(comandaRepository).deleteById(idComanda);
    }

    @Test
    @DisplayName("deleteLinea keeps comanda when linies remain")
    void deleteLineaKeepsComandaWhenLinesRemain() {
        Integer idComanda = 102;
        Integer idProducte = 11;

        when(comandaRepository.existsById(idComanda)).thenReturn(true);
        when(liniaComandaRepository.existsById(any(LiniaComandaId.class))).thenReturn(true);
        when(liniaComandaRepository.findByComandaId(idComanda)).thenReturn(List.of(buildLinia(idComanda, 99, 1, 4.0)));

        ResponseEntity<Void> response = comandaController.deleteLinea(idComanda, idProducte);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(liniaComandaRepository).deleteById(eq(new LiniaComandaId(idComanda, idProducte)));
        verify(comandaRepository, never()).deleteById(idComanda);
    }

    private Usuari buildUsuari(Integer id) {
        Usuari usuari = new Usuari();
        usuari.setId(id);
        usuari.setNom("Client");
        usuari.setEmail("client@example.com");
        usuari.setPassword("1234");
        usuari.setRol("client");
        return usuari;
    }

    private Botiga buildBotiga(Integer id) {
        Botiga botiga = new Botiga();
        botiga.setId(id);
        botiga.setNom("Botiga Test");
        botiga.setAdreca("Carrer Test");
        return botiga;
    }

    private Producte buildProducte(Integer id) {
        Producte producte = new Producte();
        producte.setId(id);
        producte.setNom("Cafe");
        producte.setCategoria("Beguda");
        producte.setPreu(BigDecimal.valueOf(3.5));
        producte.setBotiga(buildBotiga(2));
        return producte;
    }

    private LiniaComanda buildLinia(Integer idComanda, Integer idProducte, Integer quantitat, Double preu) {
        Comanda comanda = new Comanda();
        comanda.setId(idComanda);

        Producte producte = new Producte();
        producte.setId(idProducte);

        LiniaComanda linia = new LiniaComanda();
        linia.setComanda(comanda);
        linia.setProducte(producte);
        linia.setQuantitat(quantitat);
        linia.setPreuUnitari(BigDecimal.valueOf(preu));
        return linia;
    }
}
