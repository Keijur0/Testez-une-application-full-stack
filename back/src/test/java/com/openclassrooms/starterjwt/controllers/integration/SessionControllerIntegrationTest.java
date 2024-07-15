package com.openclassrooms.starterjwt.controllers.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.services.SessionService;

@DisplayName("SessionController integration tests")
@AutoConfigureMockMvc
@SpringBootTest
public class SessionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private SessionService sessionService;

    private ObjectMapper mapper = new ObjectMapper();

    private String token;

    @BeforeEach
    public void setUp() {
        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken("yoga@studio.com", "test!1234")  
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        token = jwtUtils.generateJwtToken(auth);
    }

    @DisplayName("Find session by id with existing session")
    @Test
    public void testFindById_SessionExists() throws Exception {
        mockMvc.perform(get("/api/session/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @DisplayName("Find session by id with non-existing session")
    @Test
    public void testFindById_SessionDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/session/999")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Find session by id with invalid id format")
    @Test
    public void testFindById_InvalidFormat() throws Exception {
        mockMvc.perform(get("/api/session/a")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Find all sessions")
    @Test
    public void testFindAll() throws Exception {
        mockMvc.perform(get("/api/session")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value("1"))
                .andExpect(jsonPath("$.[1].id").value("2"));
    }

    @DisplayName("Create session")
    @Test
    public void testCreate() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Yoga Session Create");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("Yoga Session Create desc");

        mockMvc.perform(post("/api/session")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Yoga Session Create"));
    }

    @DisplayName("Update session")
    @Test
    public void testUpdate() throws Exception {
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("Teacher1")
                .lastName("Yoga1")
                .build();
        Session session = Session.builder()
                .name("Session to Update")
                .date(new Date())
                .description("Session to Update Desc")
                .teacher(teacher)
                .build();

        sessionService.create(session);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Updated");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("Updated");

        mockMvc.perform(put("/api/session/" + session.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.description").value("Updated"));
        
        sessionService.delete(session.getId());
    }

    @DisplayName("Delete session by id with existing session")
    @Test
    public void testDelete_SessionExists() throws Exception {
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("Teacher1")
                .lastName("Yoga1")
                .build();
        Session session = Session.builder()
                .name("Session to Delete")
                .date(new Date())
                .description("Session to Delete Desc")
                .teacher(teacher)
                .build();
        sessionService.create(session);

        mockMvc.perform(delete("/api/session/" + session.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Beader " + token))
                .andExpect(status().isOk());
    }

    @DisplayName("Delete session by id with non-existing session")
    @Test
    public void testDelete_SessionDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/session/9999")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());  
    }

    @DisplayName("Delete session by id with invalid id format")
    @Test
    public void testDelete_InvalidFormat() throws Exception {
        mockMvc.perform(delete("/api/session/a")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Add participation success")
    @Test
    public void testParticipate_Success() throws Exception {
        Session session = Session.builder()
                .name("Session to participate")
                .date(new Date())
                .description("Session to participate desc")
                .users(new ArrayList<>())
                .build();

        sessionService.create(session);

        mockMvc.perform(post("/api/session/" + session.getId() + "/participate/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());

        sessionService.delete(session.getId());
    }

    @DisplayName("Add participation with non existing user")
    @Test
    public void testParticipate_UserDoesNotExist() throws Exception {
        Session session = Session.builder()
                .name("Session to participate")
                .date(new Date())
                .description("Session to participate desc")
                .users(new ArrayList<>())
                .build();

        sessionService.create(session);

        mockMvc.perform(post("/api/session/" + session.getId() + "/participate/99999")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());
        
        sessionService.delete(session.getId());
    }

    @DisplayName("Add participation with user already participating")
    @Test
    public void testParticipate_UserAlreadyParticipates() throws Exception {
        User user = User.builder()
                .id(1L)
                .email("john.wick@test.com")
                .lastName("Wick")
                .firstName("John")
                .password("test!1234")
                .admin(false)
                .build();
        List<User> users = new ArrayList<>();
        users.add(user);
        Session session = Session.builder()
                .name("Session to unparticipate")
                .date(new Date())
                .description("Session to unparticipate desc")
                .users(users)
                .build();

        sessionService.create(session);

        mockMvc.perform(post("/api/session/" + session.getId() + "/participate/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isBadRequest());

        sessionService.delete(session.getId());
    }

    @DisplayName("Add participation with non existing session")
    @Test
    public void testParticipate_SessionDoesNotExist() throws Exception {
        mockMvc.perform(post("/api/session/99999/participate/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Cancel participation success")
    @Test
    public void testNoLongerParticipate_Success() throws Exception {
        User user = User.builder()
                .id(1L)
                .email("john.wick@test.com")
                .lastName("Wick")
                .firstName("John")
                .password("test!1234")
                .admin(false)
                .build();
        List<User> users = new ArrayList<>();
        users.add(user);
        Session session = Session.builder()
                .name("Session to unparticipate")
                .date(new Date())
                .description("Session to unparticipate desc")
                .users(users)
                .build();

        sessionService.create(session);

        mockMvc.perform(delete("/api/session/" + session.getId() + "/participate/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());
        
        sessionService.delete(session.getId());
    }

    @DisplayName("Cancel participation with non-existing user")
    @Test
    public void testNoLongerParticipate_UserDoesNotParticipate() throws Exception {
        Session session = Session.builder()
                .name("Session to unparticipate")
                .date(new Date())
                .description("Session to unparticipate desc")
                .users(new ArrayList<>())
                .build();

        sessionService.create(session);
        mockMvc.perform(delete("/api/session/" + session.getId() + "/participate/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isBadRequest());
        
        sessionService.delete(session.getId());
    }

    @DisplayName("Cancel participation with non-existing session")
    @Test
    public void testNoLongerParticipate_SessionDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/session/99999/participate/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());
    }

}
