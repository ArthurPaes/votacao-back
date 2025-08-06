package com.example.pautachallenge.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.pautachallenge.domain.model.Votes;
import com.example.pautachallenge.domain.model.VoteStatus;
import com.example.pautachallenge.repository.VotesRepository;

public class VotesServiceTests {

    @Mock
    private VotesRepository votesRepository;

    @InjectMocks
    private VotesService votesService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateVote_Success() {
        Votes inputVotes = new Votes();
        inputVotes.setSectionId(1L);
        inputVotes.setUserId(1L);
        inputVotes.setVote(true);
        inputVotes.setStatus(VoteStatus.ABLE_TO_VOTE);

        Votes savedVotes = new Votes();
        savedVotes.setId(1L);
        savedVotes.setSectionId(1L);
        savedVotes.setUserId(1L);
        savedVotes.setVote(true);
        savedVotes.setStatus(VoteStatus.ABLE_TO_VOTE);

        when(votesRepository.findByUserIdAndSectionId(1L, 1L)).thenReturn(Optional.empty());
        when(votesRepository.save(any(Votes.class))).thenReturn(savedVotes);

        Votes createdVote = votesService.createVote(inputVotes);

        assertNotNull(createdVote);
        assertEquals(savedVotes.getUserId(), createdVote.getUserId());
        assertEquals(savedVotes.getSectionId(), createdVote.getSectionId());
        verify(votesRepository).save(any(Votes.class));
    }

    @Test
    public void testCreateVote_ExistingVote() {
        Votes inputVotes = new Votes();
        inputVotes.setSectionId(1L);
        inputVotes.setUserId(1L);
        inputVotes.setVote(true);
        inputVotes.setStatus(VoteStatus.ABLE_TO_VOTE);

        Votes existingVotes = new Votes();
        existingVotes.setId(1L);
        existingVotes.setSectionId(1L);
        existingVotes.setUserId(1L);
        existingVotes.setVote(true);
        existingVotes.setStatus(VoteStatus.ABLE_TO_VOTE);

        when(votesRepository.findByUserIdAndSectionId(1L, 1L)).thenReturn(Optional.of(existingVotes));

        Votes createdVote = votesService.createVote(inputVotes);

        assertNull(createdVote);
        verify(votesRepository, never()).save(any(Votes.class));
    }
}