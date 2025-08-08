package com.example.pautachallenge.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.pautachallenge.domain.dto.VoteDTO;
import com.example.pautachallenge.domain.model.Votes;
import com.example.pautachallenge.service.VotesService;

@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/votes")
@RequiredArgsConstructor
public class VotesController {
    private final VotesService votesService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Votes createVote(@Valid @RequestBody VoteDTO voteDTO) {
        return votesService.createVote(voteDTO);
    }
}
