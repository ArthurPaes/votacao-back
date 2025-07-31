package com.example.pautachallenge.controller;

import java.util.Random;

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
import com.example.pautachallenge.domain.model.VoteStatus;
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
        log.info("Recebendo requisição para criar voto. Usuário: {}, Seção: {}, Voto: {}", 
                voteDTO.getUserId(), voteDTO.getSectionId(), voteDTO.getVote());
        
        Random random = new Random();
        Boolean isValidCPF = random.nextDouble() > 0.3;

        if (!isValidCPF) {
            log.warn("CPF inválido detectado para usuário: {}", voteDTO.getUserId());
            Votes invalidVote = new Votes();
            invalidVote.setUserId(voteDTO.getUserId());
            invalidVote.setSectionId(voteDTO.getSectionId());
            invalidVote.setVote(voteDTO.getVote());
            invalidVote.setStatus(VoteStatus.UNABLE_TO_VOTE);
            return invalidVote;
        }

        log.debug("CPF validado com sucesso para usuário: {}", voteDTO.getUserId());
        
        Votes votes = new Votes();
        votes.setUserId(voteDTO.getUserId());
        votes.setSectionId(voteDTO.getSectionId());
        votes.setVote(voteDTO.getVote());
        
        Votes createdVote = votesService.createVote(votes);
        
        if (createdVote != null) {
            log.info("Voto criado com sucesso. ID: {}, Usuário: {}, Seção: {}", 
                    createdVote.getId(), createdVote.getUserId(), createdVote.getSectionId());
            createdVote.setStatus(VoteStatus.ABLE_TO_VOTE);
            return createdVote;
        } else {
            log.warn("Tentativa de voto duplicado. Usuário: {}, Seção: {}", voteDTO.getUserId(), voteDTO.getSectionId());
            throw new IllegalArgumentException("Esse usuário já votou nesta seção.");
        }
    }
}
