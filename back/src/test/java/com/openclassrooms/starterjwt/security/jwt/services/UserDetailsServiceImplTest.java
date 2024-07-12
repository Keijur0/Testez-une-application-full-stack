package com.openclassrooms.starterjwt.security.jwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

@DisplayName("UserDetailsService Implementation tests")
public class UserDetailsServiceImplTest {

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Load user by username success")
    @Test
    public void testLoadUserByUsername_Success() {
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
            .id(1L)
            .email("john.wick@test.com")
            .lastName("Wick")
            .firstName("John")
            .password("test!1234")
            .admin(false)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("john.wick@test.com");

        assertNotNull(userDetails);
        assertEquals(user.getEmail(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
    }

    @DisplayName("Load user by username with non-existing user")
    @Test
    public void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        UsernameNotFoundException expcetion = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("not.found.email@test.com");
        });

        assertEquals("User Not Found with email: not.found.email@test.com", expcetion.getMessage());
    }
}
