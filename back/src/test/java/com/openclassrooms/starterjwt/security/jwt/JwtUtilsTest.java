package com.openclassrooms.starterjwt.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@DisplayName("Jwt utils tests")
public class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils;

    @Mock
    private Authentication auth;

    @Mock
    private UserDetailsImpl userDetails;

    @Mock
    private Logger logger;

    private String jwtSecret = "testSecret";

    private int jwtExpirationMs = 3600000;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);

        // Private variables injection: jwtSecret, jwtExpirationMs
        Field secretField = JwtUtils.class.getDeclaredField("jwtSecret");
        secretField.setAccessible(true);
        secretField.set(jwtUtils, "testSecret");

        Field expirationField = JwtUtils.class.getDeclaredField("jwtExpirationMs");
        expirationField.setAccessible(true);
        expirationField.set(jwtUtils, 3600000);

    }

    @DisplayName("Generate jwt token")
    @Test
    public void testGenerateJwtToken() {

        when(auth.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("john.wick@test.com");

        String token = jwtUtils.generateJwtToken(auth);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @DisplayName("Get username from jwt token")
    @Test
    public void testGetUserNameFromJwtToken() {
        String username = "john.wick@test.com";
        String token = Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();

        String extractedUsername = jwtUtils.getUserNameFromJwtToken(token);

        assertEquals(username, extractedUsername);
    }

    @DisplayName("Validate jwt token with valid token")
    @Test
    public void testValidateJwtToken_ValidToken() {
        String token = Jwts.builder()
            .setSubject("john.wick@test.com")
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertTrue(isValid);
    }

    @DisplayName("Validate jwt token with invalid signature")
    @Test
    public void testValidateJwtToken_InvalidSignature() {
        String token = Jwts.builder()
            .setSubject("john.wick@test.com")
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
            .signWith(SignatureAlgorithm.HS512, "invalidSecretKey")
            .compact();

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertFalse(isValid);
    }

    @DisplayName("Validate jwt token with malformed token")
    @Test
    public void testValidateJwtToken_MalformedToken() {
        String token = "Malformed token";

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertFalse(isValid);
    }

    @DisplayName("Validate jwt token with expired token")
    @Test
    public void testValidateJwtToken_ExpiredToken() {
        String token = Jwts.builder()
            .setSubject("john.wick@test.com")
            .setIssuedAt(new Date(System.currentTimeMillis() - jwtExpirationMs - 1000))
            .setExpiration(new Date(System.currentTimeMillis() - 1000))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertFalse(isValid);
    }

    @DisplayName("Validate jwt token with unsupported token")
    @Test
    public void testValidateJwtToken_UnsupportedToken() {
        String token = Jwts.builder()
            .setSubject("john.wick@test.com")
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
            .compact();

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertFalse(isValid);
    }

    @DisplayName("Validate jwt token with empty claims")
    @Test
    public void testValidateJwtToken_EmptyClaims() {
        String token = "";

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertFalse(isValid);
    }

}
