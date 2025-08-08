package com.example.pautachallenge.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.WebRequest;

import com.example.pautachallenge.infra.ErrorResponse;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private WebRequest webRequest;

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    public void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    public void testHandleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Email já cadastrado");

        ErrorResponse response = globalExceptionHandler.handleIllegalArgumentException(exception, webRequest);

        assertNotNull(response);
        assertEquals("Email já cadastrado", response.getMessage());
        assertEquals("VALIDATION_ERROR", response.getError());
        assertNotNull(response.getTimestamp());
    }

    @Test
    public void testHandleGenericException() {
        Exception exception = new RuntimeException("Erro inesperado");

        ErrorResponse response = globalExceptionHandler.handleGenericException(exception, webRequest);

        assertNotNull(response);
        assertEquals("Erro interno do servidor", response.getMessage());
        assertEquals("INTERNAL_ERROR", response.getError());
    }
} 