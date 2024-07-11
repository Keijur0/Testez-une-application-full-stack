package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;

@DisplayName("User Controller Tests")
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Find user by id test with valid user")
    public void testFindById_Success() {
        User user = new User();
        UserDto userDto = userMapper.toDto(user);
        user.setId(1L);

        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        ResponseEntity<?> response = userController.findById("1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Find user by id with invalid id format")
    public void testFindById_InvalidFormat() {
        ResponseEntity<?> response = userController.findById("a");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Find user by id with non-existing user")
    public void testFindById_UserNotFound() {
        when(userService.findById(1L)).thenReturn(null);

        ResponseEntity<?> response = userController.findById("1");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Delete user by id with valid user")
    public void testDelete_Success() {
        User user = new User();
        user.setId(1L);
        user.setEmail("john.wick@test.com");

        when(userDetails.getUsername()).thenReturn("john.wick@test.com");

        SecurityContextHolder.setContext(new SecurityContextImpl());
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(userDetails, null));

        when(userService.findById(1L)).thenReturn(user);

        ResponseEntity<?> response = userController.save("1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("Delete user by id with invalid id format")
    public void testDelete_InvalidFormat() {
        ResponseEntity<?> response = userController.save("a");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService, never()).delete(anyLong());
    }

    @Test
    @DisplayName("Delete user by id with non-existing user")
    public void testDelete_NotFound() {
        when(userService.findById(1L)).thenReturn(null);

        ResponseEntity<?> reponse = userController.save("1");
        assertEquals(HttpStatus.NOT_FOUND, reponse.getStatusCode());
        verify(userService, never()).delete(anyLong());
    }

    @Test
    @DisplayName("Delete user by id from different user")
    public void testDelete_Unauthorized() {
        User user = new User();
        user.setId(1L);
        user.setEmail("john.wick@test.com");

        when(userDetails.getUsername()).thenReturn("not.john.wick@test.com");

        SecurityContextHolder.setContext(new SecurityContextImpl());
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(userDetails, null));

        when(userService.findById(1L)).thenReturn(user);

        ResponseEntity<?> response = userController.save("1");
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(userService, never()).delete(anyLong());
    }

}
