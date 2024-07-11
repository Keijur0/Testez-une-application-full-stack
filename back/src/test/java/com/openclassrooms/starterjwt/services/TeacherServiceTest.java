package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

@DisplayName("TeacherService tests")
public class TeacherServiceTest {
    @InjectMocks
    TeacherService teacherService;

    @Mock
    TeacherRepository teacherRepository;

    @BeforeEach
    public void setUp() {
        teacherService = new TeacherService(teacherRepository);
    }

    @Test
    @DisplayName("Find all teachers test")
    public void testFindAll() {
        Teacher mockTeacher1 = new Teacher();
        Teacher mockTeacher2 = new Teacher();
        List<Teacher> allTeachers = new ArrayList<Teacher>();
        allTeachers.add(mockTeacher1);
        allTeachers.add(mockTeacher2);
        when(teacherRepository.findAll()).thenReturn(allTeachers);

        List<Teacher> listTeachers = teacherService.findAll();

        assertNotNull(listTeachers);
        assertEquals(allTeachers, listTeachers);
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Find teacher by id test for existing teacher")
    public void testFindById_TeacherExists() {
        Long teacherId = 1L;
        Teacher mockTeacher = new Teacher();
        mockTeacher.setId(teacherId);
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(mockTeacher));

        Teacher teacher = teacherService.findById(teacherId);

        assertNotNull(teacher);
        assertEquals(teacherId, mockTeacher.getId());
        verify(teacherRepository, times(1)).findById(teacherId);
    }

    @Test
    @DisplayName("Find teacher by id test for non-existing teacher")
    public void testFindById_TeacherDoesNotExist() {
        Long teacherId = 1L;
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        Teacher teacher = teacherService.findById(teacherId);

        assertNull(teacher);
        verify(teacherRepository, times(1)).findById(teacherId);
    }
}
