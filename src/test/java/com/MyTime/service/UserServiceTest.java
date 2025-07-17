package com.MyTime.service;

import com.MyTime.entity.User;
import com.MyTime.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        user1 = new User();
        user1.setUserId(1);
        user1.setUserName("testuser1");
        user1.setEmail("test1@example.com");
        user1.setStatus("ACTIVE");

        user2 = new User();
        user2.setUserId(2);
        user2.setUserName("testuser2");
        user2.setEmail("test2@example.com");
        user2.setStatus("ACTIVE");
    }

    @Test
    void getByUserName_Found() {
        when(userRepository.findByUserName("testuser1")).thenReturn(Optional.of(user1));

        Optional<User> foundUser = userService.getByUserName("testuser1");

        assertTrue(foundUser.isPresent());
        assertEquals("testuser1", foundUser.get().getUserName());
    }

    @Test
    void getByUserName_NotFound() {
        when(userRepository.findByUserName("nonexistent")).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.getByUserName("nonexistent");

        assertFalse(foundUser.isPresent());
    }

    @Test
    void existsByUserName_True() {
        when(userRepository.existsByUserName("testuser1")).thenReturn(true);

        boolean exists = userService.existsByUserName("testuser1");

        assertTrue(exists);
    }

    @Test
    void existsByUserName_False() {
        when(userRepository.existsByUserName("nonexistent")).thenReturn(false);

        boolean exists = userService.existsByUserName("nonexistent");

        assertFalse(exists);
    }

    @Test
    void existsByEmail_True() {
        when(userRepository.existsByEmail("test1@example.com")).thenReturn(true);

        boolean exists = userService.existsByEmail("test1@example.com");

        assertTrue(exists);
    }

    @Test
    void existsByEmail_False() {
        when(userRepository.existsByEmail("nonexistent@example.com")).thenReturn(false);

        boolean exists = userService.existsByEmail("nonexistent@example.com");

        assertFalse(exists);
    }

    @Test
    void save() {
        userService.save(user1);

        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void listByCompanyId() {
        when(userRepository.findByCompanyCompanyId(1)).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.listByCompanyId(1);

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("testuser1", users.get(0).getUserName());
    }

    @Test
    void getById() {
        when(userRepository.getByUserId(1)).thenReturn(user1);

        User foundUser = userService.getById(1);

        assertNotNull(foundUser);
        assertEquals("testuser1", foundUser.getUserName());
    }

    @Test
    void existsById_True() {
        when(userRepository.existsByUserId(1)).thenReturn(true);

        boolean exists = userService.existsById(1);

        assertTrue(exists);
    }

    @Test
    void existsById_False() {
        when(userRepository.existsByUserId(1)).thenReturn(false);

        boolean exists = userService.existsById(1);

        assertFalse(exists);
    }

    @Test
    void existsByEmailAndStatus_True() {
        when(userRepository.existsByEmailAndStatus("test1@example.com", "ACTIVE")).thenReturn(true);

        boolean exists = userService.existsByEmailAndStatus("test1@example.com");

        assertTrue(exists);
    }

    @Test
    void existsByEmailAndStatus_False() {
        when(userRepository.existsByEmailAndStatus("test1@example.com", "ACTIVE")).thenReturn(false);

        boolean exists = userService.existsByEmailAndStatus("test1@example.com");

        assertFalse(exists);
    }

    @Test
    void getByEmailAndStatus() {
        when(userRepository.getByEmailAndStatus("test1@example.com", "ACTIVE")).thenReturn(user1);

        User foundUser = userService.getByEmailAndStatus("test1@example.com");

        assertNotNull(foundUser);
        assertEquals("test1@example.com", foundUser.getEmail());
    }

    @Test
    void existsByGreaterThanEqualExpirationTokenAndToken_True() {
        Date now = new Date();
        when(userRepository.existsByExpirationTokenBeforeAndToken(now, "token123")).thenReturn(true);

        boolean exists = userService.existsByGreaterThanEqualExpirationTokenAndToken(now, "token123");

        assertTrue(exists);
    }

    @Test
    void existsByGreaterThanEqualExpirationTokenAndToken_False() {
        Date now = new Date();
        when(userRepository.existsByExpirationTokenBeforeAndToken(now, "token123")).thenReturn(false);

        boolean exists = userService.existsByGreaterThanEqualExpirationTokenAndToken(now, "token123");

        assertFalse(exists);
    }

    @Test
    void getByGreaterThanEqualExpirationTokenAndToken() {
        Date now = new Date();
        when(userRepository.findByExpirationTokenBeforeAndToken(now, "token123")).thenReturn(user1);

        User foundUser = userService.getByGreaterThanEqualExpirationTokenAndToken(now, "token123");

        assertNotNull(foundUser);
        assertEquals("testuser1", foundUser.getUserName());
    }
}
