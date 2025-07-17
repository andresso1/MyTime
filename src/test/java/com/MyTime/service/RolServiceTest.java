package com.MyTime.service;

import com.MyTime.entity.Rol;
import com.MyTime.enums.RolName;
import com.MyTime.repository.RolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RolServiceTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolService rolService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getByRolName_Found() {
        Rol rol = new Rol(RolName.ROLE_USER);
        when(rolRepository.findByRolName(RolName.ROLE_USER)).thenReturn(Optional.of(rol));

        Optional<Rol> foundRol = rolService.getByRolName(RolName.ROLE_USER);

        assertTrue(foundRol.isPresent());
        assertEquals(RolName.ROLE_USER, foundRol.get().getRolName());
    }

    @Test
    void getByRolName_NotFound() {
        when(rolRepository.findByRolName(RolName.ROLE_ADMIN)).thenReturn(Optional.empty());

        Optional<Rol> foundRol = rolService.getByRolName(RolName.ROLE_ADMIN);

        assertFalse(foundRol.isPresent());
    }

    @Test
    void save() {
        Rol rol = new Rol(RolName.ROLE_USER);
        rolService.save(rol);

        verify(rolRepository, times(1)).save(rol);
    }
}
