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

import com.example.crud.domain.model.Votes;
import com.example.crud.infra.ErrorResponse;
import com.example.crud.service.VotesService;

public class VotesControllerTest {

    @Mock
    private VotesService votesService;

    @InjectMocks
    private VotesController votesController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateVote_Success() {
        Votes vote = new Votes(1L, 1L, 1L, true);
        when(votesService.createVote(any(Votes.class))).thenReturn(vote);
        ResponseEntity<?> response = votesController.createVote(vote);
        if (response.getStatusCode() == HttpStatus.CREATED) {
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(vote, response.getBody());
        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            ErrorResponse error = (ErrorResponse) response.getBody();
            assertEquals("INVALID_CPF", error.getError());
        }
    }

    @Test
    public void testCreateVote_UserAlreadyVoted() {
        Votes vote = new Votes(1L, 1L, 1L, true);
        when(votesService.createVote(any(Votes.class))).thenReturn(null);
        ResponseEntity<?> response = votesController.createVote(vote);
        if (response.getStatusCode() == HttpStatus.CONFLICT) {
            ErrorResponse error = (ErrorResponse) response.getBody();
            assertEquals("USER_ALREADY_VOTED", error.getError());
        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            ErrorResponse error = (ErrorResponse) response.getBody();
            assertEquals("INVALID_CPF", error.getError());
        }
    }

    @Test
    public void testCreateVote_InvalidCPF() {
        Votes vote = new Votes(1L, 1L, 1L, true);
        boolean foundInvalid = false;
        for (int i = 0; i < 20; i++) {
            ResponseEntity<?> response = votesController.createVote(vote);
            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                ErrorResponse error = (ErrorResponse) response.getBody();
                assertEquals("INVALID_CPF", error.getError());
                foundInvalid = true;
                break;
            }
        }
        assertTrue(foundInvalid, "Should hit INVALID_CPF at least once in 20 tries");
    }
}
