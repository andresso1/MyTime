package com.MyTime.controller;

import com.MyTime.dto.LoginUsuario;
import com.MyTime.dto.NuevoUsuario;
import com.MyTime.entity.User;
import com.MyTime.security.jwt.JwtProvider;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import com.MyTime.enums.RolName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserService userService;
    @Mock
    private RolService rolService;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private EmailService emailService;
    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(request.getServerName()).thenReturn("localhost");
    }

    @Test
    void nuevo() {
        NuevoUsuario nuevoUsuario = new NuevoUsuario();
        nuevoUsuario.setNombreUsuario("testuser");
        nuevoUsuario.setEmail("test@test.com");
        nuevoUsuario.setPassword("password");
        nuevoUsuario.setNameCompany("Test Company");

        when(userService.existsByUserName("testuser")).thenReturn(false);
        when(userService.existsByEmail("test@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        when(rolService.getByRolName(RolName.ROLE_USER)).thenReturn(java.util.Optional.of(new com.MyTime.entity.Rol(RolName.ROLE_USER)));
        when(rolService.getByRolName(RolName.ROLE_ADMIN)).thenReturn(java.util.Optional.of(new com.MyTime.entity.Rol(RolName.ROLE_ADMIN)));

        ResponseEntity<?> response = authController.nuevo(nuevoUsuario, mock(BindingResult.class));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void login() {
        LoginUsuario loginUsuario = new LoginUsuario();
        loginUsuario.setNombreUsuario("testuser");
        loginUsuario.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mock(UserDetails.class));
        when(jwtProvider.generateToken(authentication)).thenReturn("testToken");

        ResponseEntity<?> response = authController.login(loginUsuario, mock(BindingResult.class));

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void recoveryPassword() throws MessagingException, IOException {
        String email = "test@test.com";
        User user = new User();
        user.setEmail(email);

        when(userService.existsByEmail(email)).thenReturn(true);
        when(userService.existsByEmailAndStatus(email)).thenReturn(true);
        when(userService.getByEmailAndStatus(email)).thenReturn(user);

        ResponseEntity<?> response = authController.recoveryPassword(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
