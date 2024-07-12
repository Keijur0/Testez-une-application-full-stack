package com.openclassrooms.starterjwt.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;

@DisplayName("User mapper tests")
public class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @DisplayName("To Dto test")
    @Test
    public void testToDto() {
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
            .id(1L)
            .email("john.wick@test.com")
            .lastName("Wick")
            .firstName("John")
            .password("test!1234")
            .admin(false)
            .createdAt(now)
            .updatedAt(now)
            .build();
        
        UserDto userDto = userMapper.toDto(user);

        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getPassword(), userDto.getPassword());
        assertEquals(user.isAdmin(), userDto.isAdmin());
        assertEquals(user.getCreatedAt(), userDto.getCreatedAt());
        assertEquals(user.getUpdatedAt(), userDto.getUpdatedAt());
    }

    @DisplayName("To Entity test")
    @Test
    public void testToEntity() {
        LocalDateTime now = LocalDateTime.now();
        UserDto userDto = new UserDto(
            1L,
            "john.wick@test.com",
            "Wick",
            "John",
            true,
            "test!1234",
            now,
            now
        );

        User user = userMapper.toEntity(userDto);

        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getLastName(), user.getLastName());
        assertEquals(userDto.getFirstName(), user.getFirstName());
        assertEquals(userDto.isAdmin(), user.isAdmin());
        assertEquals(userDto.getCreatedAt(), user.getCreatedAt());
        assertEquals(userDto.getUpdatedAt(), user.getUpdatedAt());

    }
}
