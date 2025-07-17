package com.MyTime.security.jwt;

import com.MyTime.entity.PrimaryUser;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtProviderTest {

    @InjectMocks
    private JwtProvider jwtProvider;

    @Mock
    private Authentication authentication;

    @Mock
    private PrimaryUser primaryUser;

    // Mocks for Jwts static methods (requires PowerMock or Mockito-inline, which we are avoiding)
    // For this test, we will mock the behavior of the chained calls as much as possible.

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(jwtProvider, "secret", "testSecret");
        ReflectionTestUtils.setField(jwtProvider, "expiration", 3600);
    }

    @Test
    void generateToken() {
        when(authentication.getPrincipal()).thenReturn(primaryUser);
        when(primaryUser.getUsername()).thenReturn("testuser");
        when(primaryUser.getOrgId()).thenReturn(123);
        when(primaryUser.getUserId()).thenReturn(456);

        // Due to the static nature of Jwts.builder(), direct mocking is hard without PowerMock.
        // We will focus on verifying the inputs to the builder if it were mockable.
        // For now, we'll just ensure the method runs without error and returns a non-null string.
        String token = jwtProvider.generateToken(authentication);
        assertNotNull(token);
        // Further assertions would require parsing the token, which is what the other methods do.
    }

    @Test
    void getNombreUsuarioFromToken() {
        String testToken = createTestToken("testuser", 123, 456);
        String username = jwtProvider.getNombreUsuarioFromToken(testToken);
        assertEquals("testuser", username);
    }

    @Test
    void getCompanyIdFromToken() {
        String testToken = createTestToken("testuser", 123, 456);
        Integer companyId = jwtProvider.getCompanyIdFromToken(testToken);
        assertEquals(123, companyId);
    }

    @Test
    void getUserIdFromToken() {
        String testToken = createTestToken("testuser", 123, 456);
        Integer userId = jwtProvider.getUserIdFromToken(testToken);
        assertEquals(456, userId);
    }

    @Test
    void validateToken_Valid() {
        String validToken = createTestToken("testuser", 123, 456);
        assertTrue(jwtProvider.validateToken(validToken));
    }

    @Test
    void validateToken_MalformedJwtException() {
        String malformedToken = "malformed.token.string";
        assertFalse(jwtProvider.validateToken(malformedToken));
    }

    @Test
    void validateToken_ExpiredJwtException() {
        // Create an expired token
        String expiredToken = Jwts.builder().setSubject("testuser")
                .setIssuedAt(new Date(System.currentTimeMillis() - 100000))
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(SignatureAlgorithm.HS512, "testSecret")
                .compact();
        assertFalse(jwtProvider.validateToken(expiredToken));
    }

    @Test
    void validateToken_UnsupportedJwtException() {
        // Create a token with an unsupported signature algorithm
        String unsupportedToken = Jwts.builder().setSubject("testuser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.HS256, "testSecret") // Use HS256 instead of HS512
                .compact();
        assertFalse(jwtProvider.validateToken(unsupportedToken));
    }

    @Test
    void validateToken_IllegalArgumentException() {
        String illegalArgumentToken = null;
        assertFalse(jwtProvider.validateToken(illegalArgumentToken));
    }

    @Test
    void validateToken_SignatureException() {
        // Create a token with a different secret to cause SignatureException
        String wrongSignatureToken = Jwts.builder().setSubject("testuser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.HS512, "wrongSecret")
                .compact();
        assertFalse(jwtProvider.validateToken(wrongSignatureToken));
    }

    // Helper method to create a valid test token
    private String createTestToken(String username, Integer companyId, Integer userId) {
        return Jwts.builder().setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour expiration
                .signWith(SignatureAlgorithm.HS512, "testSecret")
                .claim("companyId", companyId)
                .claim("userId", userId)
                .compact();
    }
}
