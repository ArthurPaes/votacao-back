package com.example.pautachallenge.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.example.pautachallenge.domain.model.Votes;
import com.example.pautachallenge.repository.VotesRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class VotesService {
    private final VotesRepository repository;

    public Votes createVote(Votes votes) {
        log.debug("Verificando se já existe voto para usuário: {} na seção: {}", votes.getUserId(), votes.getSectionId());
        Optional<Votes> existingVote = repository.findByUserIdAndSectionId(votes.getUserId(), votes.getSectionId());
        if (existingVote.isPresent()) {
            log.warn("Voto já existe para usuário: {} na seção: {}", votes.getUserId(), votes.getSectionId());
            return null;
        }

        log.debug("Salvando novo voto para usuário: {} na seção: {}", votes.getUserId(), votes.getSectionId());
        Votes savedVote = repository.save(votes);
        log.debug("Voto salvo com sucesso. ID: {}, Usuário: {}, Seção: {}", 
                savedVote.getId(), savedVote.getUserId(), savedVote.getSectionId());
        return savedVote;
    }
}
