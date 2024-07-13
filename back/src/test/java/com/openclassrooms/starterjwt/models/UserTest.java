package com.openclassrooms.starterjwt.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("User model tests")
public class UserTest {

    @DisplayName("All args constructor test")
    @Test
    public void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User(1L, 
                "john.wick@test.com", 
                "Wick", 
                "John", 
                "test!1234", 
                true, 
                now, 
                now);

        assertEquals(1L, user.getId());
        assertEquals("john.wick@test.com", user.getEmail());
        assertEquals("Wick", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertEquals("test!1234", user.getPassword());
        assertTrue(user.isAdmin());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @DisplayName("Required args constructor test")
    @Test
    public void testRequiredArgsConstructor() {
        User user = new User("john.wick@test.com", "Wick", "John", "test!1234", true);

        assertEquals("john.wick@test.com", user.getEmail());
        assertEquals("Wick", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertEquals("test!1234", user.getPassword());
        assertTrue(user.isAdmin());
    }

    @DisplayName("No args constructor test")
    @Test
    public void testNoArgsConstructor() {
        User user = new User();

        assertNull(user.getId());
        assertNull(user.getEmail());
        assertNull(user.getLastName());
        assertNull(user.getFirstName());
        assertNull(user.getPassword());
        assertFalse(user.isAdmin());
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
    }

    @DisplayName("Getters and setters test")
    @Test
    public void testGettersAndSetters() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        user.setId(1L);
        user.setEmail("john.wick@test.com");
        user.setLastName("Wick");
        user.setFirstName("John");
        user.setPassword("test!1234");
        user.setAdmin(true);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        assertEquals(1L, user.getId());
        assertEquals("john.wick@test.com", user.getEmail());
        assertEquals("Wick", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertEquals("test!1234", user.getPassword());
        assertTrue(user.isAdmin());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @DisplayName("Equals and hashcode tests")
    @Test
    public void testEqualsAndHashCode() {
        User user1 = new User("john.wick@test.com", "Wick", "John", "test!1234", true);
        user1.setId(1L);

        User user2 = new User("john.wick@test.com", "Wick", "John", "test!1234", true);
        user2.setId(1L);

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());

        user2.setId(2L);
        assertNotEquals(user1, user2);
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @DisplayName("To string test")
    @Test
    public void testToString() {
        User user = new User("john.wick@test.com", "Wick", "John", "test!1234", true);
        user.setId(1L);
        String expected = "User(id=1, email=john.wick@test.com, lastName=Wick, firstName=John, password=test!1234, admin=true, createdAt=null, updatedAt=null)";
        assertEquals(expected, user.toString());
    }

    @DisplayName("Builder test")
    @Test
    public void testBuilder() {
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                        .id(1L)
                        .email("john.wick@test.com")
                        .lastName("Wick")
                        .firstName("John")
                        .password("test!1234")
                        .admin(true)
                        .createdAt(now)
                        .updatedAt(now)
                        .build();

        assertEquals(1L, user.getId());
        assertEquals("john.wick@test.com", user.getEmail());
        assertEquals("Wick", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertEquals("test!1234", user.getPassword());
        assertTrue(user.isAdmin());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }
}
