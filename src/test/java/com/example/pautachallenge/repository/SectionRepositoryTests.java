package com.example.pautachallenge.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.pautachallenge.domain.model.Section;

@DataJpaTest
@ActiveProfiles("test")
class SectionRepositoryTests {

    @Autowired
    private SectionRepository sectionRepository;

    @Test
    public void testSaveSection() {
        Section section = new Section();
        section.setName("Test Section");
        section.setDescription("Test Description");
        section.setExpiration(10);
        section.setStart_at(LocalDateTime.now());

        Section savedSection = sectionRepository.save(section);
        
        assertNotNull(savedSection);
        assertNotNull(savedSection.getId());
        assertEquals("Test Section", savedSection.getName());
        assertEquals("Test Description", savedSection.getDescription());
        assertEquals(10, savedSection.getExpiration());
    }

    @Test
    public void testFindAll() {
        List<Section> sections = sectionRepository.findAll();
        assertNotNull(sections);
        // Como não há dados no banco de teste, esperamos uma lista vazia
        assertEquals(0, sections.size());
    }
} 