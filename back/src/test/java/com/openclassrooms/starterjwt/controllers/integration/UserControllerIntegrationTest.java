package com.openclassrooms.starterjwt.controllers.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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

    @Autowired 
    private UserRepository userRepository;

    private String token;

    @BeforeEach
    public void setUp() {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken("yoga@studio.com", "test!1234")  
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"));
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

        // Creating a new user to authenticate with and to delete
        ObjectMapper mapper = new ObjectMapper();
        SignupRequest register = new SignupRequest();
        register.setEmail("testuser@test.com");
        register.setFirstName("test");
        register.setLastName("test");
        register.setPassword("test!1234");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(register)));

        User user = userRepository.findByEmail("testuser@test.com").orElseThrow(null);

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken("testuser@test.com", "test!1234")  
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        token = jwtUtils.generateJwtToken(auth);

        mockMvc.perform(delete("/api/user/" + user.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());
    }

    @DisplayName("Delete user by id with existing but unauthorized user")
    @Test
    public void testDelete_UserExistsButNotAuthorized() throws Exception {
        User user = User.builder()
                .email("testuser@test.com")
                .lastName("test")
                .firstName("test")
                .password("test!1234")
                .admin(false)
                .build();

        userRepository.save(user);

        mockMvc.perform(delete("/api/user/" + user.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isUnauthorized());
        
        userRepository.deleteById(user.getId());
    }

    @DisplayName("Delete user by id with non-existing user")
    @Test
    public void testDelete_UserDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/user/999999")
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
