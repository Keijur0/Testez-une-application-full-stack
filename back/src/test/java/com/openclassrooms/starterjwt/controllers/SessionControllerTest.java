package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;

@DisplayName("Session Controller tests")
public class SessionControllerTest {
    
    @InjectMocks
    SessionController sessionController;

    @Mock
    SessionService sessionService;

    @Mock
    SessionMapper sessionMapper;

    private Session session;
    private SessionDto sessionDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        session = new Session();
        session.setId(1L);
        sessionDto = new SessionDto();
        sessionDto.setId(1L);
    }

    @DisplayName("Find session by id test success")
    @Test
    public void testFindById_Success() {
        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.findById("1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    @DisplayName("Find session by id test for non-existing session")
    @Test
    public void testFindById_SessionNotFound() {
        when(sessionService.getById(1L)).thenReturn(null);

        ResponseEntity<?> response = sessionController.findById("1");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @DisplayName("Find session by id test for invalid id format")
    @Test
    public void testFindById_InvalidFormat() {
        ResponseEntity<?> response = sessionController.findById("a");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @DisplayName("Find all sessions test")
    @Test
    public void testFindAll() {
        List<Session> sessions = new ArrayList<Session>();
        sessions.add(session);
        List<SessionDto> sessionDtoList = new ArrayList<SessionDto>();
        sessionDtoList.add(sessionDto);

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtoList);

        ResponseEntity<?> response = sessionController.findAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDtoList, response.getBody());
    }

    @DisplayName("Create session test")
    @Test
    public void testCreate() {
     when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
     when(sessionService.create(session)).thenReturn(session);
     when(sessionMapper.toDto(session)).thenReturn(sessionDto);

     ResponseEntity<?> response = sessionController.create(sessionDto);
     assertEquals(HttpStatus.OK, response.getStatusCode());
     assertEquals(sessionDto, response.getBody());
    }

    @DisplayName("Update session test success")
    @Test
    public void testUpdate_Success() {
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(1L, session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.update("1", sessionDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    @DisplayName("Update session test invalid id format")
    @Test
    public void testUpdate_InvalidFormat() {
        ResponseEntity<?> response = sessionController.update("a", sessionDto);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @DisplayName("Delete session test success")
    @Test
    public void testDelete_Success() {
        when(sessionService.getById(1L)).thenReturn(session);

        ResponseEntity<?> response = sessionController.save("1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService, times(1)).delete(1L);
    }

    @DisplayName("Delete session test for not found session")
    @Test
    public void testDelete_SessionNotFound() {
        when(sessionService.getById(1L)).thenReturn(null);

        ResponseEntity<?> response = sessionController.save("1");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(sessionService, never()).delete(1L);
    }

    @DisplayName("Delete session test for invalid id format")
    @Test
    public void testDelete_InvalidFormat() {
        ResponseEntity<?> response = sessionController.save("a");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(sessionService, never()).delete(anyLong());
    }

    @DisplayName("Add participation test success")
    @Test
    public void testParticipate_Success() {
        ResponseEntity<?> response = sessionController.participate("1", "1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService, times(1)).participate(1L, 1L);
    }

    @DisplayName("Add participation test for invalid id format")
    @Test
    public void testParticipate_InvalidFormat() {
        ResponseEntity<?> response = sessionController.participate("a", "1");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(sessionService, never()).participate(anyLong(), anyLong());
    }

    @DisplayName("Cancel participation test success")
    @Test
    public void testNoLongerParticipate_Success() {
        ResponseEntity<?> response = sessionController.noLongerParticipate("1", "1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService, times(1)).noLongerParticipate(1L, 1L);
    }


}
