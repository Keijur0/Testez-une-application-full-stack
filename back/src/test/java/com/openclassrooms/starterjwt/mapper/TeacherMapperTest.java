package com.openclassrooms.starterjwt.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;

@DisplayName("Teacher mapper tests")
public class TeacherMapperTest {

    private TeacherMapper teacherMapper;

    @BeforeEach
    public void setUp() {
        teacherMapper = Mappers.getMapper(TeacherMapper.class);
        
    }

    @DisplayName("To Dto test")
    @Test
    public void testToDto() {
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = Teacher.builder()
            .id(1L)
            .lastName("Wick")
            .firstName("John")
            .createdAt(now)
            .updatedAt(now)
            .build();

        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        assertEquals(teacher.getId(), teacherDto.getId());
        assertEquals(teacher.getLastName(), teacherDto.getLastName());
        assertEquals(teacher.getFirstName(), teacherDto.getFirstName());
        assertEquals(teacher.getCreatedAt(), teacherDto.getCreatedAt());
        assertEquals(teacher.getUpdatedAt(), teacherDto.getUpdatedAt());
    }

    @DisplayName("To Entity test")
    @Test
    public void testToEntity() {
        LocalDateTime now = LocalDateTime.now();
        TeacherDto teacherDto = new TeacherDto(
            1L,
            "Wick",
            "John",
            now,
            now
        );

        Teacher teacher = teacherMapper.toEntity(teacherDto);

        assertEquals(teacherDto.getId(), teacher.getId());
        assertEquals(teacherDto.getLastName(), teacher.getLastName());
        assertEquals(teacherDto.getFirstName(), teacher.getFirstName());
        assertEquals(teacherDto.getCreatedAt(), teacher.getCreatedAt());
        assertEquals(teacherDto.getUpdatedAt(), teacher.getUpdatedAt());
    }
}
