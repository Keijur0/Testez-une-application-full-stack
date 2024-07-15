package com.openclassrooms.starterjwt.security.jwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

@DisplayName("UserDetailsImpl tests")
public class UserDetailsImplTest {

    private UserDetailsImpl userDetails;

    @BeforeEach
    public void setUp() {
        userDetails = UserDetailsImpl.builder() 
            .id(1L)
            .username("john.wick@test.com")
            .firstName("John")
            .lastName("Wick")
            .admin(true)
            .password("test!1234")
            .build();
    }

    @DisplayName("Getters tests")
    public void testGetters() {
        assertEquals(1L, userDetails.getId());
        assertEquals("john.wick@test.com", userDetails.getUsername());
        assertEquals("John", userDetails.getFirstName());
        assertEquals("Wick", userDetails.getLastName());
        assertEquals(true, userDetails.getAdmin());
        assertEquals("test!1234", userDetails.getPassword());
    }

    @DisplayName("Get authorities test")
    @Test
    public void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertTrue(authorities instanceof HashSet);
        assertTrue(authorities.isEmpty());
    }

    @DisplayName("Is account non-expired test")
    @Test
    public void testIsAccountNonExpired() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    @DisplayName("Is account non-locked test")
    @Test
    public void testIsAccountNonLocked() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    @DisplayName("Is credentials non-expired test")
    @Test
    public void testIsCredentialsNonExpired() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @DisplayName("Is enabled test")
    @Test
    public void testIsEnabled() {
        assertTrue(userDetails.isEnabled());
    }

    @DisplayName("Equal tests")
    @Test
    public void testEquals() {
        UserDetailsImpl userDetails1 = UserDetailsImpl.builder()
                .id(1L)
                .username("testuser1")
                .firstName("Test1")
                .lastName("User1")
                .admin(false)
                .password("password1")
                .build();

        UserDetailsImpl userDetails2 = UserDetailsImpl.builder()
                .id(1L)
                .username("testuser2")
                .firstName("Test2")
                .lastName("User2")
                .admin(true)
                .password("password2")
                .build();

        UserDetailsImpl userDetails3 = UserDetailsImpl.builder()
                .id(2L)
                .username("testuser3")
                .firstName("Test3")
                .lastName("User3")
                .admin(true)
                .password("password3")
                .build();

        UserDetailsImpl userDetails4 = null;

        User user = User.builder()
                .id(1L)
                .email("testuser1")
                .firstName("Test1")
                .lastName("User1")
                .admin(false)
                .password("password1")
                .build();


        assertFalse(userDetails1.getAdmin());
        assertTrue(userDetails2.getAdmin());
        assertEquals(userDetails1, userDetails2);
        assertNotEquals(userDetails1, userDetails3);
        assertNotEquals(userDetails1, userDetails4);
        assertNotEquals(userDetails1, user);
    }
}
