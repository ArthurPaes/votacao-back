package com.example.pautachallenge.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.pautachallenge.domain.dto.UserDTO;
import com.example.pautachallenge.domain.dto.UserResponseDTO;
import com.example.pautachallenge.service.UserService;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserControllerTests {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testCreateUser_Success() {
        UserDTO userDTO = new UserDTO("John Doe", "123456789", "password", "test@example.com");
        UserResponseDTO userResponse = new UserResponseDTO(1L, "John Doe", "123456789", "test@example.com");

        when(userService.createUser(any(UserDTO.class))).thenReturn(userResponse);

        UserResponseDTO result = userController.createUser(userDTO);

        assertNotNull(result);
        assertEquals(userResponse.id(), result.id());
        assertEquals(userResponse.email(), result.email());
    }

    @Test
    public void testCreateUser_ThrowsException() {
        UserDTO userDTO = new UserDTO("John Doe", "123456789", "password", "test@example.com");

        when(userService.createUser(any(UserDTO.class)))
            .thenThrow(new IllegalArgumentException("Email já cadastrado"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userController.createUser(userDTO);
        });

        assertEquals("Email já cadastrado", exception.getMessage());
    }

}