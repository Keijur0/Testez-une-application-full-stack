package com.openclassrooms.starterjwt.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

@DisplayName("Session mapper tests")
public class SessionMapperTest {

    @InjectMocks
    private SessionMapper sessionMapper = Mappers.getMapper(SessionMapper.class);

    @Mock
    TeacherService teacherService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("To Dto test")
    @Test
    public void testToDto() {
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

    @DisplayName("To Entity test")
    @Test
    public void testToEntity() {
        LocalDateTime now = LocalDateTime.now();
        Date date = new Date();
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        List<Long> users = new ArrayList<Long>();

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
        assertEquals(sessionDto.getUsers(), session.getUsers());
        assertEquals(sessionDto.getCreatedAt(), session.getCreatedAt());
        assertEquals(sessionDto.getUpdatedAt(), session.getUpdatedAt());
    }

}
