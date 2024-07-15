package com.openclassrooms.starterjwt.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Session model tests")
public class SessionTest {

    @DisplayName("All args constructor test")
    @Test
    public void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        Date sessionDate = new Date();
        Teacher teacher = new Teacher();
        User user1 = new User();
        User user2 = new User();
        List<User> users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);

        Session session = new Session(1L, "Session 1", sessionDate, "Description", teacher, users, now, now);

        assertEquals(1L, session.getId());
        assertEquals("Session 1", session.getName());
        assertEquals(sessionDate, session.getDate());
        assertEquals("Description", session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertEquals(users, session.getUsers());
        assertEquals(now, session.getCreatedAt());
        assertEquals(now, session.getUpdatedAt());
    }

    @DisplayName("No args constructor test")
    @Test
    public void testNoArgsConstructor() {
        Session session = new Session();

        assertNull(session.getId());
        assertNull(session.getName());
        assertNull(session.getDate());
        assertNull(session.getDescription());
        assertNull(session.getTeacher());
        assertNull(session.getUsers());
        assertNull(session.getCreatedAt());
        assertNull(session.getUpdatedAt());
    }

    @DisplayName("Getters and setters tests")
    @Test
    public void testGettersAndSetters() {
        LocalDateTime now = LocalDateTime.now();
        Date sessionDate = new Date();
        Teacher teacher = new Teacher();
        User user1 = new User();
        User user2 = new User();
        List<User> users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);

        Session session = new Session();
        session.setId(1L);
        session.setName("Session 1");
        session.setDate(sessionDate);
        session.setDescription("Description");
        session.setTeacher(teacher);
        session.setUsers(users);
        session.setCreatedAt(now);
        session.setUpdatedAt(now);

        assertEquals(1L, session.getId());
        assertEquals("Session 1", session.getName());
        assertEquals(sessionDate, session.getDate());
        assertEquals("Description", session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertEquals(users, session.getUsers());
        assertEquals(now, session.getCreatedAt());
        assertEquals(now, session.getUpdatedAt());
    }

    @DisplayName("Equals and hashcode tests")
    @Test
    public void testEqualsAndHashCode() {
        Date sessionDate = new Date();
        Teacher teacher = new Teacher();
        User user1 = new User();
        User user2 = new User();
        List<User> users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);

        Session session1 = new Session(1L, "Session 1", sessionDate, "Description", teacher, users, null, null);
        Session session2 = new Session(1L, "Session 1", sessionDate, "Description", teacher, users, null, null);

        assertEquals(session1, session2);
        assertEquals(session1.hashCode(), session2.hashCode());

        session2.setId(2L);
        assertNotEquals(session1, session2);
        assertNotEquals(session1.hashCode(), session2.hashCode());
    }

    @DisplayName("To string test")
    @Test
    public void testToString() {
        Date sessionDate = new Date();
        Teacher teacher = new Teacher();
        User user1 = new User();
        User user2 = new User();
        List<User> users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);

        Session session = new Session(1L, "Session 1", sessionDate, "Description", teacher, users, null, null);
        String expected = "Session(id=1, name=Session 1, date=" + sessionDate + ", description=Description, teacher=" + teacher + ", users=" + users + ", createdAt=null, updatedAt=null)";
        assertEquals(expected, session.toString());
    }

    @DisplayName("Builder test")
    @Test
    public void testBuilder() {
        LocalDateTime now = LocalDateTime.now();
        Date sessionDate = new Date();
        Teacher teacher = new Teacher();
        User user1 = new User();
        User user2 = new User();
        List<User> users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);

        Session session = Session.builder()
                .id(1L)
                .name("Session 1")
                .date(sessionDate)
                .description("Description")
                .teacher(teacher)
                .users(users)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(1L, session.getId());
        assertEquals("Session 1", session.getName());
        assertEquals(sessionDate, session.getDate());
        assertEquals("Description", session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertEquals(users, session.getUsers());
        assertEquals(now, session.getCreatedAt());
        assertEquals(now, session.getUpdatedAt());
    }
}
