package com.openclassrooms.starterjwt.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;

@DisplayName("Session mapper tests")
public class SessionMapperTest {

    @InjectMocks
    private SessionMapper sessionMapper = Mappers.getMapper(SessionMapper.class);

    @Mock
    TeacherService teacherService;

    @Mock
    UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("To Dto test with session not null")
    @Test
    public void testToDto_SessionNotNull() {
        LocalDateTime now = LocalDateTime.now();
        Date date = new Date();
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        List<User> users = new ArrayList<User>(); 

        Session session = Session.builder()
            .id(1L)
            .name("Yoga Session")
            .date(date)
            .description("Yoga Session Description")
            .teacher(teacher)
            .users(users)
            .createdAt(now)
            .updatedAt(now)
            .build();

        SessionDto SessionDto = sessionMapper.toDto(session);

        assertEquals(session.getId(), SessionDto.getId());
        assertEquals(session.getName(), SessionDto.getName());
        assertEquals(session.getDate(), SessionDto.getDate());
        assertEquals(session.getDescription(), SessionDto.getDescription());
        assertEquals(session.getTeacher().getId(), SessionDto.getTeacher_id());
        assertEquals(session.getUsers(), SessionDto.getUsers());
        assertEquals(session.getCreatedAt(), SessionDto.getCreatedAt());
        assertEquals(session.getUpdatedAt(), SessionDto.getUpdatedAt());
    }

    @DisplayName("To Dto test with session null")
    @Test
    public void testToDto_SessionIsNull() {
        Session session = null;
        SessionDto sessionDto = sessionMapper.toDto(session);

        assertNull(sessionDto);
    }

    @DisplayName("To Entity test with sessionDto not null")
    @Test
    public void testToEntity_SessionDtoNotNull() {
        LocalDateTime now = LocalDateTime.now();
        Date date = new Date();
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        List<Long> users = new ArrayList<Long>();
        users.add(1L);
        users.add(2L);

        SessionDto sessionDto = new SessionDto(
            1L,
            "Yoga Session 1",
            date,
            1L,
            "Yoga Session Description",
            users,
            now,
            now
        );

        when(teacherService.findById(1L)).thenReturn(teacher);

        Session session = sessionMapper.toEntity(sessionDto);

        assertEquals(sessionDto.getId(), session.getId());
        assertEquals(sessionDto.getName(), session.getName());
        assertEquals(sessionDto.getDate(), session.getDate());
        assertEquals(sessionDto.getTeacher_id(), session.getTeacher().getId());
        assertEquals(sessionDto.getDescription(), session.getDescription());
        assertEquals(sessionDto.getUsers().size(), session.getUsers().size());
        assertEquals(sessionDto.getCreatedAt(), session.getCreatedAt());
        assertEquals(sessionDto.getUpdatedAt(), session.getUpdatedAt());
    }

    @DisplayName("To Entity test with sessionDto null")
    @Test
    public void testToEntity_SessionDtoIsNull() {
        SessionDto sessionDto = null;
        Session session = sessionMapper.toEntity(sessionDto);

        assertNull(session);
    }

    @DisplayName("To Dto List test with session list not null")
    @Test
    public void testToDtoList_SessionListNotNull() {
        LocalDateTime now = LocalDateTime.now();
        Date date = new Date();
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        List<User> users = new ArrayList<User>();

        Session session1 = Session.builder()
                .id(1L)
                .name("Yoga Session 1")
                .date(date)
                .description("Yoga Session 1 Description")
                .teacher(teacher)
                .users(users)
                .createdAt(now)
                .updatedAt(now)
                .build();

        Session session2 = Session.builder()
                .id(2L)
                .name("Yoga Session 2")
                .date(date)
                .description("Yoga Session 2 Description")
                .teacher(teacher)
                .users(users)
                .createdAt(now)
                .updatedAt(now)
                .build();

        List<Session> sessions = new ArrayList<Session>();
        sessions.add(session1);
        sessions.add(session2);
        List<SessionDto> sessionDtoList = sessionMapper.toDto(sessions);

        assertNotNull(sessionDtoList);
        assertEquals(2, sessionDtoList.size());

        SessionDto sessionDto1 = sessionDtoList.get(0);
        assertEquals(session1.getId(), sessionDto1.getId());
        assertEquals(session1.getName(), sessionDto1.getName());
        assertEquals(session1.getDate(), sessionDto1.getDate());
        assertEquals(session1.getDescription(), sessionDto1.getDescription());
        assertEquals(session1.getTeacher().getId(), sessionDto1.getTeacher_id());
        assertEquals(session1.getUsers().size(), sessionDto1.getUsers().size());
        assertEquals(session1.getCreatedAt(), sessionDto1.getCreatedAt());
        assertEquals(session1.getUpdatedAt(), sessionDto1.getUpdatedAt());

        SessionDto sessionDto2 = sessionDtoList.get(1);
        assertEquals(session2.getId(), sessionDto2.getId());
        assertEquals(session2.getName(), sessionDto2.getName());
        assertEquals(session2.getDate(), sessionDto2.getDate());
        assertEquals(session2.getDescription(), sessionDto2.getDescription());
        assertEquals(session2.getTeacher().getId(), sessionDto2.getTeacher_id());
        assertEquals(session2.getUsers().size(), sessionDto2.getUsers().size());
        assertEquals(session2.getCreatedAt(), sessionDto2.getCreatedAt());
        assertEquals(session2.getUpdatedAt(), sessionDto2.getUpdatedAt());
    }

    @DisplayName("To Dto List test with session list null")
    @Test
    public void testToDtoList_SessionListIsNull() {
        List<Session> sessions = null;
        List<SessionDto> sessionDtoList = sessionMapper.toDto(sessions);

        assertNull(sessionDtoList);
    }

    @DisplayName("To Entity List test with sessionDto list not null")
    @Test
    public void testToEntityList_SessionDtoListNotNull() {
        LocalDateTime now = LocalDateTime.now();
        Date date = new Date();
        Teacher teacher1 = new Teacher();
        Teacher teacher2 = new Teacher();
        teacher1.setId(1L);
        teacher2.setId(2L);
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        User user3 = new User();
        user3.setId(3L);
        User user4 = new User();
        user4.setId(4L);

        List<Long> users1 = new ArrayList<Long>();
        users1.add(1L);
        users1.add(2L);
        List<Long> users2 = new ArrayList<Long>();
        users2.add(3L);
        users2.add(4L);

        SessionDto sessionDto1 = new SessionDto(
                1L,
                "Yoga Session 1",
                date,
                1L,
                "Yoga Session 1 Description",
                users1,
                now,
                now
        );

        SessionDto sessionDto2 = new SessionDto(
                2L,
                "Yoga Session 2",
                date,
                2L,
                "Yoga Session 2 Description",
                users2,
                now,
                now
        );

        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);
        when(userService.findById(3L)).thenReturn(user3);
        when(userService.findById(4L)).thenReturn(user4);
        when(teacherService.findById(1L)).thenReturn(teacher1);
        when(teacherService.findById(2L)).thenReturn(teacher2);

        List<SessionDto> sessionDtoList = new ArrayList<SessionDto>();
        sessionDtoList.add(sessionDto1);
        sessionDtoList.add(sessionDto2);
        List<Session> sessions = sessionMapper.toEntity(sessionDtoList);

        assertNotNull(sessions);
        assertEquals(2, sessions.size());


        // Mocking fetching teachers by ids
        sessions.get(0).setTeacher(teacher1);
        sessions.get(1).setTeacher(teacher2);

        Session session1 = sessions.get(0);
        assertEquals(sessionDto1.getId(), session1.getId());
        assertEquals(sessionDto1.getName(), session1.getName());
        assertEquals(sessionDto1.getDate(), session1.getDate());
        assertEquals(sessionDto1.getDescription(), session1.getDescription());
        assertEquals(sessionDto1.getTeacher_id(), session1.getTeacher().getId());
        assertEquals(sessionDto1.getUsers().size(), session1.getUsers().size());
        assertEquals(sessionDto1.getCreatedAt(), session1.getCreatedAt());
        assertEquals(sessionDto1.getUpdatedAt(), session1.getUpdatedAt());

        Session session2 = sessions.get(1);
        assertEquals(sessionDto2.getId(), session2.getId());
        assertEquals(sessionDto2.getName(), session2.getName());
        assertEquals(sessionDto2.getDate(), session2.getDate());
        assertEquals(sessionDto2.getDescription(), session2.getDescription());
        assertEquals(sessionDto2.getTeacher_id(), session2.getTeacher().getId());
        assertEquals(sessionDto2.getUsers().size(), session2.getUsers().size());
        assertEquals(sessionDto2.getCreatedAt(), session2.getCreatedAt());
        assertEquals(sessionDto2.getUpdatedAt(), session2.getUpdatedAt());
    }

    @DisplayName("To Entity List test with sessionDto list null")
    @Test
    public void testToEntityList_SessionDtoListIsNull() {
        List<SessionDto> sessionDtoList = null;
        List<Session> sessions = sessionMapper.toEntity(sessionDtoList);

        assertNull(sessions);
    }
}    
