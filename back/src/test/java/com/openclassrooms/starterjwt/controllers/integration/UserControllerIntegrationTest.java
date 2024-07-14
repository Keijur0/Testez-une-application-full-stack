package com.openclassrooms.starterjwt.controllers.integration;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.services.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@DisplayName("UserController integration tests")
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtils jwtUtils;

    @MockBean
    private UserService userService;

    private String token;

    @BeforeEach
    public void setUp() {
        LocalDateTime now = LocalDateTime.now();
        User user1 = User.builder()
                .id(1L)
                .email("john.wick@test.com")
                .lastName("Wick")
                .firstName("John")
                .password("test!1234")
                .admin(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        User user2 = User.builder()
                .id(1L)
                .email("not.john.wick@test.com")
                .lastName("Wick")
                .firstName("NotJohn")
                .password("test!1234")
                .admin(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        Mockito.when(userService.findById(1L)).thenReturn(user1);
        Mockito.when(userService.findById(2L)).thenReturn(user2);
        Mockito.when(userService.findById(3L)).thenReturn(null);

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken("john.wick@test.com", "test!1234")  
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        token = jwtUtils.generateJwtToken(auth);
    }

    @DisplayName("Find user by id test with existing user")
    @Test
    public void testFindById_UserExists() throws Exception {
        mockMvc.perform(get("/api/user/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @DisplayName("Find user by id test with non-existing user")
    @Test
    public void testFindById_UserDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/user/3")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());
    }


    @DisplayName("Find user by id with invalid id format")
    @Test
    public void testFindById_InvalidFormat() throws Exception {
        mockMvc.perform(get("/api/user/a")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Delete user by id with existing and authorized user")
    @Test
    public void testDelete_UserExistsAndAuthorized() throws Exception {
        mockMvc.perform(delete("/api/user/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());
    }

    @DisplayName("Delete user by id with existing but unauthorized user")
    @Test
    public void testDelete_UserExistsButNotAuthorized() throws Exception {
        mockMvc.perform(delete("/api/user/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isUnauthorized());

    }

    @DisplayName("Delete user by id with non-existing user")
    @Test
    public void testDelete_UserDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/user/3")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Delete user by id with invalid id format")
    @Test
    public void testDelete_InvalidFormat() throws Exception {
        mockMvc.perform(delete("/api/user/a")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

}
