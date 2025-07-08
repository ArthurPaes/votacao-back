package com.example.crud.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.crud.domain.interfaces.UserLoginRequest;
import com.example.crud.domain.model.User;
import com.example.crud.service.UserService;

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
        User mockUser = new User(1L, "John Doe", "123456789", "password", "test@example.com");

        when(userService.authenticate(anyString(), anyString())).thenReturn(true);
        when(userService.findUser(anyString())).thenReturn(mockUser);

        ResponseEntity<?> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
    }

    @Test
    public void testLogin_FailedAuthentication() {
        UserLoginRequest request = new UserLoginRequest("test@example.com", "password");

        when(userService.authenticate(anyString(), anyString())).thenReturn(false);

        ResponseEntity<?> response = authController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Credenciais invalidas!", response.getBody());
    }
}