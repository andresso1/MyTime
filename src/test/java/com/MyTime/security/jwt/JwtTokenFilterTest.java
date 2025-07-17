package com.MyTime.security.jwt;

import com.MyTime.entity.PrimaryUser;
import com.MyTime.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JwtTokenFilterTest {

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtTokenFilter jwtTokenFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        // Clear security context before each test to ensure isolation
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_ValidToken() throws ServletException, IOException {
        String token = "Bearer valid.jwt.token";
        String rawToken = "valid.jwt.token";
        String username = "testuser";
        UserDetails userDetails = new PrimaryUser("Test User", username, "test@example.com", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")), 1, 1);

        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtProvider.validateToken(rawToken)).thenReturn(true);
        when(jwtProvider.getNombreUsuarioFromToken(rawToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertTrue(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken);
        assertEquals(username, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Test
    void doFilterInternal_InvalidToken() throws ServletException, IOException {
        String token = "Bearer invalid.jwt.token";
        String rawToken = "invalid.jwt.token";

        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtProvider.validateToken(rawToken)).thenReturn(false);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_NoToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_ExceptionDuringProcessing() throws ServletException, IOException {
        String token = "Bearer valid.jwt.token";
        String rawToken = "valid.jwt.token";

        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtProvider.validateToken(rawToken)).thenThrow(new RuntimeException("Test Exception"));

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    
}
