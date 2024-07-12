package com.openclassrooms.starterjwt.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @DisplayName("To Dto test with teacher not null")
    @Test
    public void testToDto_TeacherNotNull() {
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

    @DisplayName("To Dto test with teacher null")
    @Test
    public void testToDto_TeacherIsNull() {
        Teacher teacher = null;
        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        assertNull(teacherDto);
    }

    @DisplayName("To Entity test with teacherDto not null")
    @Test
    public void testToEntity_TeacherDtoNotNull() {
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

    @DisplayName("To Entity test with teacherDto null")
    @Test
    public void testToEntity_TeacherDtoIsNull() {
        TeacherDto teacherDto = null;
        Teacher teacher = teacherMapper.toEntity(teacherDto);

        assertNull(teacher);
    }

    @DisplayName("To Dto List test with teacher list not null")
    @Test
    public void testToDtoList_TeacherListNotNull() {
        LocalDateTime now = LocalDateTime.now();

        Teacher teacher1 = Teacher.builder()
                .id(1L)
                .lastName("Wick")
                .firstName("John")
                .createdAt(now)
                .updatedAt(now)
                .build();

        Teacher teacher2 = Teacher.builder()
                .id(2L)
                .lastName("Wick")
                .firstName("NotJohn")
                .createdAt(now)
                .updatedAt(now)
                .build();

        List<Teacher> teachers = new ArrayList<Teacher>();
        teachers.add(teacher1);
        teachers.add(teacher2);
        List<TeacherDto> teacherDtos = teacherMapper.toDto(teachers);

        assertNotNull(teacherDtos);
        assertEquals(2, teacherDtos.size());

        TeacherDto teacherDto1 = teacherDtos.get(0);
        assertEquals(teacher1.getId(), teacherDto1.getId());
        assertEquals(teacher1.getLastName(), teacherDto1.getLastName());
        assertEquals(teacher1.getFirstName(), teacherDto1.getFirstName());
        assertEquals(teacher1.getCreatedAt(), teacherDto1.getCreatedAt());
        assertEquals(teacher1.getUpdatedAt(), teacherDto1.getUpdatedAt());

        TeacherDto teacherDto2 = teacherDtos.get(1);
        assertEquals(teacher2.getId(), teacherDto2.getId());
        assertEquals(teacher2.getLastName(), teacherDto2.getLastName());
        assertEquals(teacher2.getFirstName(), teacherDto2.getFirstName());
        assertEquals(teacher2.getCreatedAt(), teacherDto2.getCreatedAt());
        assertEquals(teacher2.getUpdatedAt(), teacherDto2.getUpdatedAt());
    }

    @DisplayName("To Dto List test with teacher list null")
    @Test
    public void testToDtoList_TeacherListIsNull() {
        List<Teacher> teachers = null;
        List<TeacherDto> teacherDtoList = teacherMapper.toDto(teachers);

        assertNull(teacherDtoList);
    }

    @DisplayName("To Entity List test with teacherDtoList not null")
    @Test
    public void testToEntityList_TeacherDtoListNotNull() {
        LocalDateTime now = LocalDateTime.now();

        TeacherDto teacherDto1 = new TeacherDto(
                1L,
                "Wick",
                "John",
                now,
                now
        );

        TeacherDto teacherDto2 = new TeacherDto(
                2L,
                "Doe",
                "NotJohn",
                now,
                now
        );

        List<TeacherDto> teacherDtoList = new ArrayList<TeacherDto>();
        teacherDtoList.add(teacherDto1);
        teacherDtoList.add(teacherDto2);
        List<Teacher> teachers = teacherMapper.toEntity(teacherDtoList);

        assertNotNull(teachers);
        assertEquals(2, teachers.size());

        Teacher teacher1 = teachers.get(0);
        assertEquals(teacherDto1.getId(), teacher1.getId());
        assertEquals(teacherDto1.getLastName(), teacher1.getLastName());
        assertEquals(teacherDto1.getFirstName(), teacher1.getFirstName());
        assertEquals(teacherDto1.getCreatedAt(), teacher1.getCreatedAt());
        assertEquals(teacherDto1.getUpdatedAt(), teacher1.getUpdatedAt());

        Teacher teacher2 = teachers.get(1);
        assertEquals(teacherDto2.getId(), teacher2.getId());
        assertEquals(teacherDto2.getLastName(), teacher2.getLastName());
        assertEquals(teacherDto2.getFirstName(), teacher2.getFirstName());
        assertEquals(teacherDto2.getCreatedAt(), teacher2.getCreatedAt());
        assertEquals(teacherDto2.getUpdatedAt(), teacher2.getUpdatedAt());
    }

    @DisplayName("To Entity List test with teacherDtoList null")
    @Test
    public void testToEntityList_TeacherDtoListIsNull() {
        List<TeacherDto> teacherDtoList = null;
        List<Teacher> teachers = teacherMapper.toEntity(teacherDtoList);

        assertNull(teachers);
    }
}
