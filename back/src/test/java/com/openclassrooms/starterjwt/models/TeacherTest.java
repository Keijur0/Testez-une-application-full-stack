package com.openclassrooms.starterjwt.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Teacher model tests")
public class TeacherTest {

    @DisplayName("All args constructor test")
    @Test
    public void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = new Teacher(1L, "Wick", "John", now, now);

        assertEquals(1L, teacher.getId());
        assertEquals("Wick", teacher.getLastName());
        assertEquals("John", teacher.getFirstName());
        assertEquals(now, teacher.getCreatedAt());
        assertEquals(now, teacher.getUpdatedAt());
    }

    @DisplayName("No args constructor test")
    @Test
    public void testNoArgsConstructor() {
        Teacher teacher = new Teacher();

        assertNull(teacher.getId());
        assertNull(teacher.getLastName());
        assertNull(teacher.getFirstName());
        assertNull(teacher.getCreatedAt());
        assertNull(teacher.getUpdatedAt());
    }

    @DisplayName("Getters and setters tests")
    @Test
    public void testGettersAndSetters() {
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setLastName("Wick");
        teacher.setFirstName("John");
        teacher.setCreatedAt(now);
        teacher.setUpdatedAt(now);

        assertEquals(1L, teacher.getId());
        assertEquals("Wick", teacher.getLastName());
        assertEquals("John", teacher.getFirstName());
        assertEquals(now, teacher.getCreatedAt());
        assertEquals(now, teacher.getUpdatedAt());
    }

    @DisplayName("Equals and hashcode tests")
    @Test
    public void testEqualsAndHashCode() {
        Teacher teacher1 = new Teacher(1L, "Wick", "John", null, null);
        Teacher teacher2 = new Teacher(1L, "Wick", "John", null, null);

        assertEquals(teacher1, teacher2);
        assertEquals(teacher1.hashCode(), teacher2.hashCode());

        teacher2.setId(2L);
        assertNotEquals(teacher1, teacher2);
        assertNotEquals(teacher1.hashCode(), teacher2.hashCode());
    }

    @DisplayName("To string test")
    @Test
    public void testToString() {
        Teacher teacher = new Teacher(1L, "Wick", "John", null, null);
        String expected = "Teacher(id=1, lastName=Wick, firstName=John, createdAt=null, updatedAt=null)";
        assertEquals(expected, teacher.toString());
    }

    @DisplayName("Builder test")
    @Test
    public void testBuilder() {
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = Teacher.builder()
                                 .id(1L)
                                 .lastName("Wick")
                                 .firstName("John")
                                 .createdAt(now)
                                 .updatedAt(now)
                                 .build();

        assertEquals(1L, teacher.getId());
        assertEquals("Wick", teacher.getLastName());
        assertEquals("John", teacher.getFirstName());
        assertEquals(now, teacher.getCreatedAt());
        assertEquals(now, teacher.getUpdatedAt());
    }
}
