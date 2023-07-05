package com.community.app.service;

import com.community.app.dto.UserDto;
import com.community.app.entity.User;
import com.community.app.exception.NotFoundException;
import com.community.app.mapper.UserMapper;
import com.community.app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    public static final String NOT_FOUND = "resourceNotFound";

    @Test
    public void testGetUserById() {
        // Given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setPhoneNumber("093123456");
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        userDto.setPhoneNumber("093123456");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.userToUserDto(user)).thenReturn(userDto);

        // When
        UserDto result = userService.getUserById(userId);

        // Then
        assertEquals(userId, result.getId());
        assertEquals(user.getPhoneNumber(), result.getPhoneNumber());
    }

    @Test
    public void testGetUserById_ThrowsNotFoundException() {
        // Given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(NotFoundException.class, () ->
                userService.getUserById(userId));
        String actualMessage = exception.getMessage();

        // Then
        assertEquals(NOT_FOUND, actualMessage);

    }

    @Test
    public void testGetAllUsers() {
        // Given
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<UserDto> result = userService.getAllUsers();

        // Then
        assertEquals(users.size(), result.size());
    }

    @Test
    public void testCreateUser() {
        // Given
        UserDto userDto = new UserDto();
        User user = new User();
        when(userMapper.userDtoToUser(userDto)).thenReturn(user);
        when(userMapper.userToUserDto(user)).thenReturn(userDto);
        when(userRepository.save(user)).thenReturn(user);

        // When
        UserDto result = userService.createUser(userDto);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testDeleteUser() {
        // Given
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        // When
        userService.deleteUser(userId);

        // Then
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testDeleteUser_ThrowsNotFoundException() {
        // Given
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        // When
        Exception exception = assertThrows(NotFoundException.class, () ->
                userService.deleteUser(userId));
        String actualMessage = exception.getMessage();

        // Then
        assertEquals(NOT_FOUND, actualMessage);
    }

    @Test
    public void testUpdateUser() {
        // Given
        Long userId = 1L;
        UserDto userDto = new UserDto();
        User existingUser = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userMapper.userToUserDto(existingUser)).thenReturn(userDto);
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        // When
        UserDto result = userService.updateUser(userId, userDto);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testUpdateUser_ThrowsNotFoundException() {
        // Given
        Long userId = 1L;
        UserDto userDto = new UserDto();

        // When
        Exception exception = assertThrows(NotFoundException.class, () ->
                userService.updateUser(userId, userDto));
        String actualMessage = exception.getMessage();

        // Then
        assertEquals(NOT_FOUND, actualMessage);
    }
}
