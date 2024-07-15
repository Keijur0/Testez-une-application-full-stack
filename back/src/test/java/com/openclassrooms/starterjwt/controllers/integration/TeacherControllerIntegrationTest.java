package com.openclassrooms.starterjwt.controllers.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("TeacherController integration tests")
@AutoConfigureMockMvc
@SpringBootTest
public class TeacherControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtils jwtUtils;

    private String token;

    @BeforeEach
    public void setUp() {
        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken("yoga@studio.com", "test!1234")  
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        token = jwtUtils.generateJwtToken(auth);
    }

    @DisplayName("Find teacher by id with existing teacher")
    @Test
    public void testFindById_TeacherExists() throws Exception {
        mockMvc.perform(get("/api/teacher/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"));
    }

    @DisplayName("Find teacher by id with non-existing teacher")
    @Test
    public void testFindById_TeacherDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/teacher/999")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Find teacher by id with invalid id format")
    @Test
    public void testFindById_InvalidFormat() throws Exception {
        mockMvc.perform(get("/api/teacher/a")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Find all teachers test")
    @Test
    public void testFindAll() throws Exception {
        mockMvc.perform(get("/api/teacher")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value("1"))
                .andExpect(jsonPath("$.[1].id").value("2"));;
    }
}