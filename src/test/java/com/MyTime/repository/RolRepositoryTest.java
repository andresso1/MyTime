package com.MyTime.repository;

import com.MyTime.entity.Rol;
import com.MyTime.enums.RolName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RolRepositoryTest {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Rol rolUser;
    private Rol rolAdmin;

    @BeforeEach
    void setUp() {
        rolUser = new Rol(RolName.ROLE_USER);
        rolAdmin = new Rol(RolName.ROLE_ADMIN);

        entityManager.persist(rolUser);
        entityManager.persist(rolAdmin);
        entityManager.flush();
    }

    @Test
    void findByRolName_Found() {
        Optional<Rol> foundRol = rolRepository.findByRolName(RolName.ROLE_USER);

        assertTrue(foundRol.isPresent());
        assertEquals(RolName.ROLE_USER, foundRol.get().getRolName());
    }

    @Test
    void findByRolName_NotFound() {
        Optional<Rol> foundRol = rolRepository.findByRolName(RolName.ROLE_APPROVER);

        assertFalse(foundRol.isPresent());
    }

    @Test
    void saveRol() {
        Rol newRol = new Rol(RolName.ROLE_APPROVER);
        Rol savedRol = rolRepository.save(newRol);

        assertNotNull(savedRol.getId());
        assertEquals(RolName.ROLE_APPROVER, savedRol.getRolName());

        Optional<Rol> found = rolRepository.findById(savedRol.getId());
        assertTrue(found.isPresent());
        assertEquals(RolName.ROLE_APPROVER, found.get().getRolName());
    }

    @Test
    void findById() {
        Optional<Rol> foundRol = rolRepository.findById(rolUser.getId());

        assertTrue(foundRol.isPresent());
        assertEquals(RolName.ROLE_USER, foundRol.get().getRolName());
    }

    @Test
    void existsById_True() {
        boolean exists = rolRepository.existsById(rolUser.getId());
        assertTrue(exists);
    }

    @Test
    void existsById_False() {
        boolean exists = rolRepository.existsById(999);
        assertFalse(exists);
    }
}
