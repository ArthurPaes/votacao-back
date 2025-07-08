package com.example.crud.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.crud.domain.interfaces.SectionWithVotesCount;
import com.example.crud.domain.model.Section;
import com.example.crud.service.SectionService;

public class SectionControllerTests {

    @Mock
    private SectionService sectionService;

    @InjectMocks
    private SectionController sectionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    static class SectionWithVotesCountImpl implements SectionWithVotesCount {
        private final Long id;
        private final String name;
        private final String description;
        private final Integer expiration;
        private final LocalDateTime start_at;
        private final Long totalVotes;
        private final Long votesTrue;
        private final Long votesFalse;
        private final Boolean hasVoted;
        private final Boolean isExpired;

        public SectionWithVotesCountImpl(Long id, String name, String description, Integer expiration, LocalDateTime start_at, Long totalVotes, Long votesTrue, Long votesFalse, Boolean hasVoted, Boolean isExpired) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.expiration = expiration;
            this.start_at = start_at;
            this.totalVotes = totalVotes;
            this.votesTrue = votesTrue;
            this.votesFalse = votesFalse;
            this.hasVoted = hasVoted;
            this.isExpired = isExpired;
        }
        public Long getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public Integer getExpiration() { return expiration; }
        public LocalDateTime getStart_at() { return start_at; }
        public Long getTotalVotes() { return totalVotes; }
        public Long getVotesTrue() { return votesTrue; }
        public Long getVotesFalse() { return votesFalse; }
        public Boolean getHasVoted() { return hasVoted; }
        public Boolean getIsExpired() { return isExpired; }
    }

    @Test
    public void testGetAllSections() {
        Long userId = 1L;
        SectionWithVotesCount section = new SectionWithVotesCountImpl(1L, "Section 1", "Desc", 10, LocalDateTime.now(), 5L, 3L, 2L, true, false);
        List<SectionWithVotesCount> sections = Arrays.asList(section);
        when(sectionService.getAllSectionsWithVotes(userId)).thenReturn(sections);

        ResponseEntity<List<SectionWithVotesCount>> response = sectionController.getAllSections(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sections, response.getBody());
    }

    @Test
    public void testCreateSection() {
        Section section = new Section();
        section.setName("Section 1");
        section.setDescription("Desc");
        section.setExpiration(10);
        section.setStart_at(LocalDateTime.now());

        Section createdSection = new Section();
        createdSection.setId(1L);
        createdSection.setName("Section 1");
        createdSection.setDescription("Desc");
        createdSection.setExpiration(10);
        createdSection.setStart_at(section.getStart_at());

        when(sectionService.createSection(any(Section.class))).thenReturn(createdSection);

        ResponseEntity<Section> response = sectionController.createSection(section);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdSection, response.getBody());
    }
}
