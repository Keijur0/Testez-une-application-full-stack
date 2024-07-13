package com.openclassrooms.starterjwt.payload.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class JwtResponseTest {
    @Test
    public void testConstructorAndGetters() {
        // Given
        String token = "jwt";
        Long id = 1L;
        String username = "john.wick@test.com";
        String firstName = "John";
        String lastName = "Wick";
        Boolean admin = true;

        // When
        JwtResponse jwtResponse = new JwtResponse(token, id, username, firstName, lastName, admin);

        // Then
        assertEquals(token, jwtResponse.getToken());
        assertEquals("Bearer", jwtResponse.getType());
        assertEquals(id, jwtResponse.getId());
        assertEquals(username, jwtResponse.getUsername());
        assertEquals(firstName, jwtResponse.getFirstName());
        assertEquals(lastName, jwtResponse.getLastName());
        assertEquals(admin, jwtResponse.getAdmin());
    }

    @Test
    public void testSetters() {
        // Given
        JwtResponse jwtResponse = new JwtResponse("testToken", 1L, "testUser", "Test", "User", true);

        // When
        jwtResponse.setToken("jwt");
        jwtResponse.setId(2L);
        jwtResponse.setUsername("john.wick@test.com");
        jwtResponse.setFirstName("John");
        jwtResponse.setLastName("Wick");
        jwtResponse.setAdmin(false);

        // Then
        assertEquals("jwt", jwtResponse.getToken());
        assertEquals(2L, jwtResponse.getId());
        assertEquals("john.wick@test.com", jwtResponse.getUsername());
        assertEquals("John", jwtResponse.getFirstName());
        assertEquals("Wick", jwtResponse.getLastName());
        assertEquals(false, jwtResponse.getAdmin());
    }
}
