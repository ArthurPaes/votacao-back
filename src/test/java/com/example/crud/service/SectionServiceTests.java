package com.example.crud.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.crud.domain.interfaces.SectionWithVotesCount;
import com.example.crud.domain.model.Section;
import com.example.crud.repository.SectionRepository;

public class SectionServiceTests {

    @Mock
    private SectionRepository sectionRepository;

    @InjectMocks
    private SectionService sectionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllSectionsWithVotes() {
        Long userId = 1L;
        List<SectionWithVotesCount> sections = new ArrayList<>();
        when(sectionRepository.findAllWithVotesCount(userId)).thenReturn(sections);

        List<SectionWithVotesCount> result = sectionService.getAllSectionsWithVotes(userId);

        assertEquals(sections, result);
    }
    @Test
    public void testCreateSection() {
        // Arrange
        Section section = new Section();
        section.setName("Test Section");
        section.setDescription("This is a test section");
        section.setExpiration(10);

        Section expectedSection = new Section();
        expectedSection.setId(1L);
        expectedSection.setName("Test Section");
        expectedSection.setDescription("This is a test section");
        expectedSection.setExpiration(10);
        expectedSection.setStart_at(LocalDateTime.now());

        when(sectionRepository.save(any(Section.class))).thenReturn(expectedSection); // Change to any(Section.class)

        // Act
        Section actualSection = sectionService.createSection(section);

        // Assert
        assertEquals(expectedSection, actualSection);
    }
}
