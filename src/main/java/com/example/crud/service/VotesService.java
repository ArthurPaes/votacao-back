package com.example.crud.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.crud.domain.model.Votes;
import com.example.crud.repository.VotesRepository;

@Service
public class VotesService {
    @Autowired
    private VotesRepository repository;

    public Votes createVote(Votes votes) {
        Optional<Votes> existingVote = repository.findByUserIdAndSectionId(votes.getUserId(), votes.getSectionId());
        if (existingVote.isPresent()) {
            return null;
        }

        return repository.save(votes);
    }
}
