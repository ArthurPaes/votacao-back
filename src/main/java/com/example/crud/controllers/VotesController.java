package com.example.crud.controllers;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.crud.domain.model.Votes;
import com.example.crud.infra.ErrorResponse;
import com.example.crud.service.VotesService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/votes")
public class VotesController {
    @Autowired
    private VotesService votesService;

    @PostMapping
    public ResponseEntity<?> createVote(@RequestBody Votes votes) {
        Random random = new Random();
        Boolean isValidCPF = random.nextDouble() > 0.3;

        if (!isValidCPF) {
            ErrorResponse errorResponse = new ErrorResponse(
                "CPF Inválido! Você não pode realizar a ação de votar.",
                "INVALID_CPF"
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse);
        }

        Votes createdVote = votesService.createVote(votes);
        if (createdVote != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdVote);
        } else {
            ErrorResponse errorResponse = new ErrorResponse(
                "Esse usuário já votou nesta seção.",
                "USER_ALREADY_VOTED"
            );
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse);
        }
    }
}
