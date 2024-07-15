package com.openclassrooms.starterjwt.controllers.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;

@DisplayName("Auth controller integration tests")
@AutoConfigureMockMvc
@SpringBootTest
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @DisplayName("Authenticate admin user success")
    @Test
    public void testAuthenticateAdminUser_Success() throws Exception {
        LoginRequest login = new LoginRequest();
        login.setEmail("yoga@studio.com");
        login.setPassword("test!1234");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value(login.getEmail()))
                .andExpect(jsonPath("$.admin").value("true"));
    }

    @DisplayName("Authenticate user success")
    @Test
    public void testAuthenticateUser_Success() throws Exception {
        SignupRequest register = new SignupRequest();
        register.setEmail("testuser@test.com");
        register.setFirstName("test");
        register.setLastName("test");
        register.setPassword("test!1234");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(register)))
                .andExpect(status().isOk());
                
        LoginRequest login = new LoginRequest();
        login.setEmail("testuser@test.com");
        login.setPassword("test!1234");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value(login.getEmail()))
                .andExpect(jsonPath("$.admin").value("false"));

        User user = userRepository.findByEmail("testuser@test.com").orElseThrow(null);
        userService.delete(user.getId());
    }

    @DisplayName("Register user success")
    @Test
    public void testRegisterUser_Success() throws JsonProcessingException, Exception {
        SignupRequest register = new SignupRequest();
        register.setEmail("testuser@test.com");
        register.setFirstName("test");
        register.setLastName("test");
        register.setPassword("test!1234");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(register)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));

        User user = userRepository.findByEmail("testuser@test.com").orElseThrow(null);                
        userService.delete(user.getId());
    }

    @DisplayName("Register user email already taken")
    @Test
    public void testRegisterUser_EmailTaken() throws JsonProcessingException, Exception {
        SignupRequest register = new SignupRequest();
        register.setEmail("yoga@studio.com");
        register.setFirstName("test");
        register.setLastName("test");
        register.setPassword("test!1234");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(register)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Email is already taken!"));
    }



}
