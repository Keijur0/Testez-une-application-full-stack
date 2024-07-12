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

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;

@DisplayName("User mapper tests")
public class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @DisplayName("To Dto test with user not null")
    @Test
    public void testToDto_UserNotNull() {
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

    @DisplayName("To Dto test with user null")
    @Test
    public void testToDto_UserIsNull() {
        User user = null;
        UserDto userDto = userMapper.toDto(user);

        assertNull(userDto);
    }

    @DisplayName("To Entity test with userDto not null")
    @Test
    public void testToEntity_UserNotNull() {
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

    @DisplayName("To Entity test with userDto null")
    @Test
    public void testToEntity_UserIsNull() {
        UserDto userDto = null;
        User user = userMapper.toEntity(userDto);

        assertNull(user);
    }

    @DisplayName("To Dto List test with user list not null")
    @Test
    public void testToDtoList_UserListNotNull() {
        LocalDateTime now = LocalDateTime.now();

        User user1 = User.builder()
            .id(1L)
            .email("john.wick@test.com")
            .lastName("Wick")
            .firstName("John")
            .password("test!1234")
            .admin(false)
            .createdAt(now)
            .updatedAt(now)
            .build();

        User user2 = User.builder()
            .id(2L)
            .email("not.john.wick@test.com")
            .lastName("Wick")
            .firstName("NotJohn")
            .password("test!1234")
            .admin(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        List<User> users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);
        List<UserDto> userDtoList = userMapper.toDto(users);

        assertNotNull(userDtoList);
        assertEquals(2, userDtoList.size());

        UserDto userDto1 = userDtoList.get(0);
        assertEquals(user1.getId(), userDto1.getId());
        assertEquals(user1.getEmail(), userDto1.getEmail());
        assertEquals(user1.getLastName(), userDto1.getLastName());
        assertEquals(user1.getFirstName(), userDto1.getFirstName());
        assertEquals(user1.getPassword(), userDto1.getPassword());
        assertEquals(user1.isAdmin(), userDto1.isAdmin());
        assertEquals(user1.getCreatedAt(), userDto1.getCreatedAt());
        assertEquals(user1.getUpdatedAt(), userDto1.getUpdatedAt());

        UserDto userDto2 = userDtoList.get(1);
        assertEquals(user2.getId(), userDto2.getId());
        assertEquals(user2.getEmail(), userDto2.getEmail());
        assertEquals(user2.getLastName(), userDto2.getLastName());
        assertEquals(user2.getFirstName(), userDto2.getFirstName());
        assertEquals(user2.getPassword(), userDto2.getPassword());
        assertEquals(user2.isAdmin(), userDto2.isAdmin());
        assertEquals(user2.getCreatedAt(), userDto2.getCreatedAt());
        assertEquals(user2.getUpdatedAt(), userDto2.getUpdatedAt());
    }

    @DisplayName("To Dto List test with user list null")
    @Test
    public void testToDtoList_UserListIsNull() {
        List<User> users = null;
        List<UserDto> userDtoList = userMapper.toDto(users);

        assertNull(userDtoList);
    }

    @DisplayName("To Entity List test with userDto list not null")
    @Test
    public void testToEntityList_UserDtoListNotNull() {
        LocalDateTime now = LocalDateTime.now();

        UserDto userDto1 = new UserDto(
            1L,
            "john.wick@test.com",
            "Wick",
            "John",
            true,
            "test!1234",
            now,
            now
        );

        UserDto userDto2 = new UserDto(
            1L,
            "not.john.wick@test.com",
            "Wick",
            "NotJohn",
            false,
            "test!1234",
            now,
            now
        );

        List<UserDto> userDtoList = new ArrayList<UserDto>();
        userDtoList.add(userDto1);
        userDtoList.add(userDto2);
        List<User> users = userMapper.toEntity(userDtoList);

        assertNotNull(users);
        assertEquals(2, users.size());

        User user1 = users.get(0);
        assertEquals(userDto1.getId(), user1.getId());
        assertEquals(userDto1.getEmail(), user1.getEmail());
        assertEquals(userDto1.getLastName(), user1.getLastName());
        assertEquals(userDto1.getFirstName(), user1.getFirstName());
        assertEquals(userDto1.getPassword(), user1.getPassword());
        assertEquals(userDto1.isAdmin(), user1.isAdmin());
        assertEquals(userDto1.getCreatedAt(), user1.getCreatedAt());
        assertEquals(userDto1.getUpdatedAt(), user1.getUpdatedAt());

        User user2 = users.get(1);
        assertEquals(userDto2.getId(), user2.getId());
        assertEquals(userDto2.getEmail(), user2.getEmail());
        assertEquals(userDto2.getLastName(), user2.getLastName());
        assertEquals(userDto2.getFirstName(), user2.getFirstName());
        assertEquals(userDto2.getPassword(), user2.getPassword());
        assertEquals(userDto2.isAdmin(), user2.isAdmin());
        assertEquals(userDto2.getCreatedAt(), user2.getCreatedAt());
        assertEquals(userDto2.getUpdatedAt(), user2.getUpdatedAt());
    }

    @DisplayName("To Entity List test with userDto list null")
    @Test
    public void testToEntityList_UserDtoListIsNull() {
        List<UserDto> userDtoList = null;
        List<User> users = userMapper.toEntity(userDtoList);

        assertNull(users);
    }
}
