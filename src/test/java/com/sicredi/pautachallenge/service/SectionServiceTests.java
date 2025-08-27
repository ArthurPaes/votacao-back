package com.sicredi.pautachallenge.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sicredi.pautachallenge.domain.dto.SectionDTO;
import com.sicredi.pautachallenge.domain.interfaces.SectionWithVotesCount;
import com.sicredi.pautachallenge.domain.model.Section;
import com.sicredi.pautachallenge.repository.SectionRepository;

@ExtendWith(MockitoExtension.class)
class SectionServiceTests {

    @Mock
    private SectionRepository sectionRepository;

    @InjectMocks
    private SectionService sectionService;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testGetAllSectionsWithVotes() {
        Long userId = 1L;
        List<SectionWithVotesCount> sections = new ArrayList<>();
        when(sectionRepository.findAllWithVotesCount(userId)).thenReturn(sections);

        List<SectionWithVotesCount> result = sectionService.getAllSectionsWithVotes(userId);

        assertEquals(sections, result);
        verify(sectionRepository).findAllWithVotesCount(userId);
    }

    @Test
    public void testCreateSection() {
        SectionDTO sectionDTO = new SectionDTO("Test Section", "This is a test section", 10);

        Section expectedSection = new Section();
        expectedSection.setId(1L);
        expectedSection.setName("Test Section");
        expectedSection.setDescription("This is a test section");
        expectedSection.setExpiration(10);
        expectedSection.setStart_at(LocalDateTime.now());

        when(sectionRepository.save(any(Section.class))).thenReturn(expectedSection);

        Section actualSection = sectionService.createSection(sectionDTO);

        assertNotNull(actualSection);
        assertEquals(expectedSection.getId(), actualSection.getId());
        assertEquals(expectedSection.getName(), actualSection.getName());
        verify(sectionRepository).save(any(Section.class));
    }
}
