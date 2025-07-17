package com.MyTime.controller;

import com.MyTime.dto.UserDto;
import com.MyTime.entity.Company;
import com.MyTime.entity.User;
import com.MyTime.security.jwt.JwtProvider;
import com.MyTime.service.CompanyService;
import com.MyTime.service.EmailService;
import com.MyTime.service.RolService;
import com.MyTime.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.mail.MessagingException;
import java.io.IOException;
import org.springframework.validation.BindingResult;
import com.MyTime.enums.RolName;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private CompanyService companyService;
    @Mock
    private RolService rolService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void listByCompanyId() {
        when(companyService.existsById(1)).thenReturn(true);
        when(userService.listByCompanyId(1)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = userController.listByCompanyId(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void create() throws MessagingException, IOException {
        UserDto userDto = new UserDto();
        userDto.setUserName("testuser");
        userDto.setEmail("test@test.com");
        userDto.setPassword("password");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        when(jwtProvider.generateToken(authentication)).thenReturn("testToken");
        when(jwtProvider.getCompanyIdFromToken("testToken")).thenReturn(1);
        when(companyService.getByCompanyId(1)).thenReturn(new Company());
        when(userService.existsByUserName("testuser")).thenReturn(false);
        when(userService.existsByEmail("test@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        when(rolService.getByRolName(RolName.ROLE_USER)).thenReturn(java.util.Optional.of(new com.MyTime.entity.Rol(RolName.ROLE_USER)));
        when(rolService.getByRolName(RolName.ROLE_ADMIN)).thenReturn(java.util.Optional.of(new com.MyTime.entity.Rol(RolName.ROLE_ADMIN)));

        ResponseEntity<?> response = userController.create(userDto, mock(BindingResult.class));

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getById() {
        User user = new User();
        user.setUserId(1);
        user.setUserName("existingUser");

        when(userService.existsById(1)).thenReturn(true);
        when(userService.getById(1)).thenReturn(user);

        ResponseEntity<User> response = userController.getById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void update() {
        UserDto userDto = new UserDto();
        userDto.setUserName("testuser");
        userDto.setEmail("test@test.com");
        userDto.setPassword("password");

        User user = new User();
        user.setUserId(1);
        user.setUserName("existingUser");
        user.setEmail("existing@user.com");
        user.setRoles(new java.util.HashSet<>(java.util.Arrays.asList(new com.MyTime.entity.Rol(RolName.ROLE_ADMIN))));

        when(userService.getById(1)).thenReturn(user);
        when(userService.existsByUserName("testuser")).thenReturn(false);
        when(userService.existsByEmail("test@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(rolService.getByRolName(RolName.ROLE_ADMIN)).thenReturn(java.util.Optional.of(new com.MyTime.entity.Rol(RolName.ROLE_ADMIN)));
        when(rolService.getByRolName(RolName.ROLE_USER)).thenReturn(java.util.Optional.of(new com.MyTime.entity.Rol(RolName.ROLE_USER)));
        when(rolService.getByRolName(RolName.ROLE_APPROVER)).thenReturn(java.util.Optional.of(new com.MyTime.entity.Rol(RolName.ROLE_APPROVER)));

        ResponseEntity<?> response = userController.update(1, userDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
