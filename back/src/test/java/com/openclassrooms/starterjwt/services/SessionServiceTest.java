package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;



@DisplayName("SessionService tests")
public class SessionServiceTest {

    @InjectMocks
    private SessionService sessionService;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        sessionService = new SessionService(sessionRepository, userRepository);
    }

    @Test
    @DisplayName("Create session test")
    public void testCreate() {
        Session mockSession = new Session();

        when(sessionRepository.save(mockSession)).thenReturn(mockSession);

        sessionService.create(mockSession);

        verify(sessionRepository, times(1)).save(mockSession);
    }

    @Test
    @DisplayName("Delete session by id test")
    public void testDelete() {
        Long sessionId = 1L;

        sessionService.delete(sessionId);

        verify(sessionRepository, times(1)).deleteById(sessionId);
    }

    @Test
    @DisplayName("Find all sessions test")
    public void testFindAll() {
        Session mockSession1 = new Session();
        Session mockSession2 = new Session();

        List<Session> allSessions = new ArrayList<Session>();
        allSessions.add(mockSession1);
        allSessions.add(mockSession2);

        when(sessionRepository.findAll()).thenReturn(allSessions);

        List<Session> listSessions = sessionService.findAll();

        assertNotNull(listSessions);
        assertEquals(allSessions, listSessions);
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get session by id test for existing session test")
    public void testGetById_SessionExists() {
        Long sessionId = 1L;
        Session mockSession = new Session();
        mockSession.setId(sessionId);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));

        Session session = sessionService.getById(sessionId);

        assertNotNull(session);
        assertEquals(sessionId, session.getId());
        verify(sessionRepository, times(1)).findById(sessionId);
    }

    @Test
    @DisplayName("Get session by id test for non-existing session test")
    public void testGetById_SessionDoesNotExist() {
        Long sessionId = 1L;

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        Session session = sessionService.getById(sessionId);

        assertNull(session);
        verify(sessionRepository, times(1)).findById(sessionId);
    }

    @Test
    @DisplayName("Update session by id test")
    public void testUpdate() {
        Long sessionId = 1L;
        Session mockSession = new Session();
        mockSession.setId(sessionId);
        mockSession.setName("Session name");
        
        Session mockUpdatedSession = new Session();
        mockUpdatedSession.setName("Updated Session name");

        when(sessionRepository.save(mockUpdatedSession)).thenReturn(mockUpdatedSession);

        Session sessionUpdate = sessionService.update(sessionId, mockUpdatedSession);

        assertEquals(mockSession, sessionUpdate);
        verify(sessionRepository, times(1)).save(mockUpdatedSession);
    }

    @Test
    @DisplayName("Participate to session test")
    public void testParticipate() {
        Long userId = 1L;
        Long sessionId = 1L;
        User mockUser = new User();
        Session mockSession = new Session();
        mockUser.setId(userId);
        mockSession.setId(sessionId);
        mockSession.setUsers(new ArrayList<User>());

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        sessionService.participate(sessionId, userId);

        assertTrue(mockSession.getUsers().contains(mockUser));
        verify(sessionRepository, times(1)).save(mockSession);
    }

    @Test
    @DisplayName("Participate to session test for non-existing user")
    public void testParticipate_UserDoesNotExist() {
        Long sessionId = 1L;
        Session mockSession = new Session();
        mockSession.setId(sessionId);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, 1L));
    }

    @Test
    @DisplayName("Participate to session test for non-existing session")
    public void testParticipate_SessionDoesNotExist() {
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);

        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, userId));
    }

    @Test
    @DisplayName("Participate to session test for non-existing user and session")
    public void testParticipate_UserAndSessionDoNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
    }

    @Test
    @DisplayName("Participate to session test for user already participating")
    public void testParticipate_UserAlreadyParticipating() {
        Long userId = 1L;
        Long sessionId = 1L;
        User mockUser = new User();
        Session mockSession = new Session();
        mockUser.setId(userId);
        mockSession.setId(sessionId);
        List<User> usersList = new ArrayList<User>();
        usersList.add(mockUser);
        mockSession.setUsers(usersList);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));

        assertThrows(BadRequestException.class, () -> sessionService.participate(sessionId, userId));
    }

    @Test
    @DisplayName("Cancel participation to session test")
    public void testNoLongerParticipate() {
        Long userId = 1L;
        Long sessionId = 1L;
        User mockUser = new User();
        Session mockSession = new Session();
        mockUser.setId(userId);
        mockSession.setId(sessionId);
        List<User> usersList = new ArrayList<User>();
        usersList.add(mockUser);
        mockSession.setUsers(usersList);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));

        sessionService.noLongerParticipate(sessionId, userId);

        assertFalse(mockSession.getUsers().contains(mockUser));
        verify(sessionRepository, times(1)).save(mockSession);
    }

    @Test
    @DisplayName("Cancel participation to session test for non-existing user")
    public void testNoLongerParticipate_UserDoesNotExist() {
        Long sessionId = 1L;
        Session mockSession = new Session();
        mockSession.setId(sessionId);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(sessionId, 1L));
    }

    @Test
    @DisplayName("Cancel participation to session test for non-existing session")
    public void testNoLongerParticipate_SessionDoesNotExist() {
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);

        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(1L, userId));
    }

    @Test
    @DisplayName("Cancel participation to session test for user not already participating")
    public void testNoLongerParticipate_UserNotAlreadyParticipating() {
        Long userId = 1L;
        Long sessionId = 1L;
        User mockUser = new User();
        Session mockSession = new Session();
        mockUser.setId(userId);
        mockSession.setId(sessionId);
        mockSession.setUsers(new ArrayList<User>());

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));

        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(sessionId, userId));
    }

}
