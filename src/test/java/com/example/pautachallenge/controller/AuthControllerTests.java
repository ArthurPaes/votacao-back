package com.example.pautachallenge.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.pautachallenge.domain.interfaces.UserLoginRequest;
import com.example.pautachallenge.domain.dto.UserResponseDTO;
import com.example.pautachallenge.service.UserService;

public class AuthControllerTests {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogin_SuccessfulAuthentication() {
        UserLoginRequest request = new UserLoginRequest("test@example.com", "password");
        UserResponseDTO mockUser = new UserResponseDTO(1L, "John Doe", "123456789", "test@example.com");

        doNothing().when(userService).authenticate(anyString(), anyString());
        when(userService.findUser(anyString())).thenReturn(mockUser);

        UserResponseDTO result = authController.login(request);

        assertEquals(mockUser, result);
        assertEquals(mockUser.getId(), result.getId());
        assertEquals(mockUser.getEmail(), result.getEmail());
    }

    @Test
    public void testLogin_FailedAuthentication() {
        UserLoginRequest request = new UserLoginRequest("test@example.com", "password");

        doThrow(new IllegalArgumentException("Credenciais inválidas!")).when(userService).authenticate(anyString(), anyString());

        assertThrows(IllegalArgumentException.class, () -> {
            authController.login(request);
        });
    }
}