package com.example.crud.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.crud.domain.model.Votes;
import com.example.crud.repository.VotesRepository;

@ExtendWith(MockitoExtension.class)
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
        Votes votes = new Votes(1L, 1L, 1L, true);

        when(votesRepository.findByUserIdAndSectionId(anyLong(), anyLong())).thenReturn(Optional.empty());

        when(votesRepository.save(any(Votes.class))).thenReturn(votes);

        Votes createdVote = votesService.createVote(votes);

        assertNotNull(createdVote);
        assertEquals(votes.getUserId(), createdVote.getUserId());
        assertEquals(votes.getSectionId(), createdVote.getSectionId());
    }

    @Test
    public void testCreateVote_ExistingVote() {
        Votes votes =new Votes(1L, 1L, 1L, true);

        when(votesRepository.findByUserIdAndSectionId(anyLong(), anyLong())).thenReturn(Optional.of(votes));

        Votes createdVote = votesService.createVote(votes);

        assertNull(createdVote);
    }
}