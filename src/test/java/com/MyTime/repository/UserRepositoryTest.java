package com.MyTime.repository;

import com.MyTime.entity.Company;
import com.MyTime.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Company company1;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        company1 = new Company("Test Company", "Address", "12345", "NIT");
        entityManager.persist(company1);

        user1 = new User("User One", "userone", "userone@example.com", "password", "ACTIVE");
        user1.setCompany(company1);
        entityManager.persist(user1);

        user2 = new User("User Two", "usertwo", "usertwo@example.com", "password", "ACTIVE");
        user2.setCompany(company1);
        entityManager.persist(user2);

        entityManager.flush();
    }

    @Test
    void findByUserName_Found() {
        Optional<User> foundUser = userRepository.findByUserName("userone");
        assertTrue(foundUser.isPresent());
        assertEquals("userone", foundUser.get().getUserName());
    }

    @Test
    void findByUserName_NotFound() {
        Optional<User> foundUser = userRepository.findByUserName("nonexistent");
        assertFalse(foundUser.isPresent());
    }

    @Test
    void existsByUserName_True() {
        boolean exists = userRepository.existsByUserName("userone");
        assertTrue(exists);
    }

    @Test
    void existsByUserName_False() {
        boolean exists = userRepository.existsByUserName("nonexistent");
        assertFalse(exists);
    }

    @Test
    void existsByEmail_True() {
        boolean exists = userRepository.existsByEmail("userone@example.com");
        assertTrue(exists);
    }

    @Test
    void existsByEmail_False() {
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");
        assertFalse(exists);
    }

    @Test
    void findByCompanyCompanyId() {
        List<User> users = userRepository.findByCompanyCompanyId(company1.getCompanyId());
        assertNotNull(users);
        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getUserName().equals("userone")));
        assertTrue(users.stream().anyMatch(u -> u.getUserName().equals("usertwo")));
    }

    @Test
    void existsByUserId_True() {
        boolean exists = userRepository.existsByUserId(user1.getUserId());
        assertTrue(exists);
    }

    @Test
    void existsByUserId_False() {
        boolean exists = userRepository.existsByUserId(999);
        assertFalse(exists);
    }

    @Test
    void getByUserId() {
        User foundUser = userRepository.getByUserId(user1.getUserId());
        assertNotNull(foundUser);
        assertEquals("userone", foundUser.getUserName());
    }

    @Test
    void existsByEmailAndStatus_True() {
        boolean exists = userRepository.existsByEmailAndStatus("userone@example.com", "ACTIVE");
        assertTrue(exists);
    }

    @Test
    void existsByEmailAndStatus_False() {
        boolean exists = userRepository.existsByEmailAndStatus("userone@example.com", "INACTIVE");
        assertFalse(exists);
    }

    @Test
    void getByEmailAndStatus() {
        User foundUser = userRepository.getByEmailAndStatus("userone@example.com", "ACTIVE");
        assertNotNull(foundUser);
        assertEquals("userone@example.com", foundUser.getEmail());
    }

    @Test
    void existsByExpirationTokenBeforeAndToken_True() {
        user1.setToken("validToken");
        user1.setExpirationToken(new Date(System.currentTimeMillis() - 100000)); // Past date
        entityManager.persistAndFlush(user1);

        boolean exists = userRepository.existsByExpirationTokenBeforeAndToken(new Date(), "validToken");
        assertTrue(exists);
    }

    @Test
    void existsByExpirationTokenBeforeAndToken_False_ExpiredToken() {
        user1.setToken("expiredToken");
        user1.setExpirationToken(new Date(System.currentTimeMillis() + 100000)); // Future date
        entityManager.persistAndFlush(user1);

        boolean exists = userRepository.existsByExpirationTokenBeforeAndToken(new Date(), "expiredToken");
        assertFalse(exists);
    }

    @Test
    void existsByExpirationTokenBeforeAndToken_False_WrongToken() {
        user1.setToken("correctToken");
        user1.setExpirationToken(new Date(System.currentTimeMillis() + 100000));
        entityManager.persistAndFlush(user1);

        boolean exists = userRepository.existsByExpirationTokenBeforeAndToken(new Date(), "wrongToken");
        assertFalse(exists);
    }

    @Test
    void findByExpirationTokenBeforeAndToken_Found() {
        user1.setToken("findToken");
        user1.setExpirationToken(new Date(System.currentTimeMillis() - 100000));
        entityManager.persistAndFlush(user1);

        User foundUser = userRepository.findByExpirationTokenBeforeAndToken(new Date(), "findToken");
        assertNotNull(foundUser);
        assertEquals("findToken", foundUser.getToken());
    }

    @Test
    void findByExpirationTokenBeforeAndToken_NotFound() {
        user1.setToken("noFindToken");
        user1.setExpirationToken(new Date(System.currentTimeMillis() + 100000));
        entityManager.persistAndFlush(user1);

        User foundUser = userRepository.findByExpirationTokenBeforeAndToken(new Date(), "noFindToken");
        assertNull(foundUser);
    }
}
