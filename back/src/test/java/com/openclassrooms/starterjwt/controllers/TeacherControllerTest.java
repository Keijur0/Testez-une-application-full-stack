package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;

@DisplayName("Teacher Controller Tests")
public class TeacherControllerTest {

    @InjectMocks
    private TeacherController teacherController;

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Find teacher by id success")
    public void testFindById_Success() {
        Teacher teacher = new Teacher();
        TeacherDto teacherDto = teacherMapper.toDto(teacher);
        teacher.setId(1L);

        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        ResponseEntity<?> response = teacherController.findById("1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(teacherService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Find teacher by id with non-existing teacher")
    public void testFindById_TeacherNotFound() {
        when(teacherService.findById(1L)).thenReturn(null);

        ResponseEntity<?> response = teacherController.findById("1");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(teacherService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Find teacher by id with invalid id format")
    public void testFindById_InvalidFormat() {
        ResponseEntity<?> response = teacherController.findById("a");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Find all teachers")
    public void testFindAll() {
        Teacher teacher1 = new Teacher();
        Teacher teacher2 = new Teacher();
        List<Teacher> teachers = new ArrayList<Teacher>();
        teachers.add(teacher1);
        teachers.add(teacher2);
        List<TeacherDto> teachersDto = teacherMapper.toDto(teachers);

        when(teacherService.findAll()).thenReturn(teachers);

        ResponseEntity<?> response = teacherController.findAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(teachersDto, response.getBody());
    }
}
