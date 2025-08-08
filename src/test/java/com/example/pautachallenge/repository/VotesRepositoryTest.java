package com.example.pautachallenge.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.pautachallenge.domain.model.VoteStatus;
import com.example.pautachallenge.domain.model.Votes;

@DataJpaTest
@ActiveProfiles("test")
class VotesRepositoryTest {

    @Autowired
    private VotesRepository votesRepository;

    @Test
    public void testSaveVote() {
        Votes vote = new Votes();
        vote.setUserId(1L);
        vote.setSectionId(1L);
        vote.setVote(true);
        vote.setStatus(VoteStatus.ABLE_TO_VOTE);

        Votes savedVote = votesRepository.save(vote);
        
        assertNotNull(savedVote);
        assertNotNull(savedVote.getId());
        assertEquals(1L, savedVote.getUserId());
        assertEquals(1L, savedVote.getSectionId());
        assertTrue(savedVote.getVote());
        assertEquals(VoteStatus.ABLE_TO_VOTE, savedVote.getStatus());
    }

    @Test
    public void testFindByUserIdAndSectionId() {
        Votes vote = new Votes();
        vote.setUserId(1L);
        vote.setSectionId(1L);
        vote.setVote(true);
        vote.setStatus(VoteStatus.ABLE_TO_VOTE);

        votesRepository.save(vote);
        
        Optional<Votes> foundVote = votesRepository.findByUserIdAndSectionId(1L, 1L);
        assertTrue(foundVote.isPresent());
        assertEquals(1L, foundVote.get().getUserId());
        assertEquals(1L, foundVote.get().getSectionId());
    }

    @Test
    public void testFindByUserIdAndSectionIdNotFound() {
        Optional<Votes> foundVote = votesRepository.findByUserIdAndSectionId(999L, 999L);
        assertFalse(foundVote.isPresent());
    }

    @Test
    public void testFindAll() {
        List<Votes> votes = votesRepository.findAll();
        assertNotNull(votes);
        // Como não há dados no banco de teste, esperamos uma lista vazia
        assertEquals(0, votes.size());
    }
} 