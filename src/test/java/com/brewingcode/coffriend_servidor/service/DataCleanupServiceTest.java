package com.brewingcode.coffriend_servidor.service;

import com.brewingcode.coffriend_servidor.entities.Botiga;
import com.brewingcode.coffriend_servidor.entities.Usuari;
import com.brewingcode.coffriend_servidor.repositories.BotigaRepository;
import com.brewingcode.coffriend_servidor.repositories.ComandaRepository;
import com.brewingcode.coffriend_servidor.repositories.InsigniaRepository;
import com.brewingcode.coffriend_servidor.repositories.LiniaComandaRepository;
import com.brewingcode.coffriend_servidor.repositories.ProducteRepository;
import com.brewingcode.coffriend_servidor.repositories.UsuariInsigniaRepository;
import com.brewingcode.coffriend_servidor.repositories.UsuariRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("DataCleanupService Tests")
class DataCleanupServiceTest {

    @Mock
    private UsuariRepository usuariRepository;

    @Mock
    private ComandaRepository comandaRepository;

    @Mock
    private BotigaRepository botigaRepository;

    @Mock
    private ProducteRepository producteRepository;

    @Mock
    private InsigniaRepository insigniaRepository;

    @Mock
    private UsuariInsigniaRepository usuariInsigniaRepository;

    @Mock
    private LiniaComandaRepository liniaComandaRepository;

    @InjectMocks
    private DataCleanupService dataCleanupService;

    @Test
    @DisplayName("deleteAllData keeps admin users and deletes non-admin users")
    void deleteAllDataKeepsAdminUsers() {
        Usuari admin = buildUser("admin@example.com", "admin");
        admin.setBotiga(new Botiga());
        admin.setNivell(5);
        admin.setPunts(999);
        admin.setIsDemo(true);

        Usuari client = buildUser("client@example.com", "client");

        when(usuariRepository.findAll()).thenReturn(List.of(admin, client));

        dataCleanupService.deleteAllData();

        verify(usuariRepository).saveAll(List.of(admin));
        verify(usuariRepository).deleteAll(List.of(client));
        verify(usuariRepository, never()).deleteAll();

        assertNull(admin.getBotiga());
        assertNull(admin.getNivell());
        assertNull(admin.getPunts());
        assertFalse(admin.getIsDemo());
    }

    @Test
    @DisplayName("deleteDemoDataOnly never deletes demo admin")
    void deleteDemoDataOnlyDoesNotDeleteAdmin() {
        Usuari demoAdmin = buildUser("admin@example.com", "admin");
        demoAdmin.setIsDemo(true);
        demoAdmin.setComandes(List.of());
        demoAdmin.setInsignies(List.of());

        Usuari demoClient = buildUser("demo-client@example.com", "client");
        demoClient.setIsDemo(true);
        demoClient.setComandes(List.of());
        demoClient.setInsignies(List.of());

        when(usuariRepository.findByIsDemo(true)).thenReturn(List.of(demoAdmin, demoClient));
        when(botigaRepository.findByIsDemo(true)).thenReturn(List.of());
        when(insigniaRepository.findByIsDemo(true)).thenReturn(List.of());

        dataCleanupService.deleteDemoDataOnly();

        verify(usuariRepository).deleteAll(List.of(demoClient));
        verify(usuariRepository, never()).deleteAll(List.of(demoAdmin));
    }

    private Usuari buildUser(String email, String rol) {
        Usuari usuari = new Usuari();
        usuari.setEmail(email);
        usuari.setNom(email);
        usuari.setPassword("1234");
        usuari.setRol(rol);
        usuari.setComandes(List.of());
        usuari.setInsignies(List.of());
        usuari.setIsDemo(false);
        return usuari;
    }
}
