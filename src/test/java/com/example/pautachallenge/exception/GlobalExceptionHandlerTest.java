package com.example.pautachallenge.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import com.example.pautachallenge.infra.ErrorResponse;

public class GlobalExceptionHandlerTest {

    @Mock
    private WebRequest webRequest;

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    public void testHandleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Email já cadastrado");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleIllegalArgumentException(exception, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Email já cadastrado", response.getBody().getMessage());
        assertEquals("VALIDATION_ERROR", response.getBody().getError());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    public void testHandleGenericException() {
        Exception exception = new RuntimeException("Erro inesperado");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGenericException(exception, webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Erro interno do servidor", response.getBody().getMessage());
        assertEquals("INTERNAL_ERROR", response.getBody().getError());
    }
} 