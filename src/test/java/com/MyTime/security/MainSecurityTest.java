package com.MyTime.security;

import com.MyTime.security.jwt.JwtEntryPoint;
import com.MyTime.security.jwt.JwtTokenFilter;
import com.MyTime.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class MainSecurityTest {

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private JwtEntryPoint jwtEntryPoint;

    @InjectMocks
    private MainSecurity mainSecurity;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Mock
    private org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer<AuthenticationManagerBuilder, UserDetailsServiceImpl> daoAuthenticationConfigurer;

    @Mock
    private HttpSecurity httpSecurity;

    @Mock
    private CorsConfigurer<HttpSecurity> corsConfigurer;
    @Mock
    private CsrfConfigurer<HttpSecurity> csrfConfigurer;
    @Mock
    private ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequestsRegistry;
    @Mock
    private ExceptionHandlingConfigurer<HttpSecurity> exceptionHandlingConfigurer;
    @Mock
    private SessionManagementConfigurer<HttpSecurity> sessionManagementConfigurer;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        // Mock chained calls for HttpSecurity
        when(httpSecurity.cors()).thenReturn(corsConfigurer);
        when(corsConfigurer.and()).thenReturn(httpSecurity);
        when(httpSecurity.csrf()).thenReturn(csrfConfigurer);
        when(csrfConfigurer.disable()).thenReturn(httpSecurity);
        
        // Mock authorizeRequests() to return a generic RequestMatcherRegistry
        ExpressionUrlAuthorizationConfigurer.AuthorizedUrl authorizedUrlMock = mock(ExpressionUrlAuthorizationConfigurer.AuthorizedUrl.class);
        when(httpSecurity.authorizeRequests()).thenReturn(authorizeRequestsRegistry);
        when(authorizeRequestsRegistry.antMatchers(any(String[].class))).thenReturn(authorizedUrlMock);
        when(exceptionHandlingConfigurer.authenticationEntryPoint(any(JwtEntryPoint.class))).thenReturn(exceptionHandlingConfigurer);
        when(exceptionHandlingConfigurer.and()).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement()).thenReturn(sessionManagementConfigurer);
        when(sessionManagementConfigurer.sessionCreationPolicy(any())).thenReturn(sessionManagementConfigurer);
    }

    @Test
    void jwtTokenFilter_BeanCreation() {
        JwtTokenFilter jwtTokenFilter = mainSecurity.jwtTokenFilter();
        assertNotNull(jwtTokenFilter);
    }

    @Test
    void passwordEncoder_BeanCreation() {
        PasswordEncoder passwordEncoder = mainSecurity.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    void configure_AuthenticationManagerBuilder() throws Exception {
        mainSecurity.configure(authenticationManagerBuilder);
        verify(authenticationManagerBuilder, times(1)).userDetailsService(userDetailsService);
        verify(daoAuthenticationConfigurer, times(1)).passwordEncoder(any(PasswordEncoder.class));
    }

    @Test
    void authenticationManagerBean() throws Exception {
        // This method just calls super.authenticationManagerBean(), which is hard to test directly.
        // We can at least verify that it doesn't throw an exception and returns an AuthenticationManager.

        AuthenticationManager authenticationManager = mainSecurity.authenticationManagerBean();
        assertNotNull(authenticationManager);
    }
}