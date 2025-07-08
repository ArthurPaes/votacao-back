package com.example.crud.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.crud.domain.interfaces.SectionWithVotesCount;

public class SectionRepositoryTests {

    @Mock
    private SectionRepository sectionRepositoryMock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAllWithVotesCount() {
        Long userId = 1L;
        List<SectionWithVotesCount> sections = new ArrayList<>();
        when(sectionRepositoryMock.findAllWithVotesCount(userId)).thenReturn(sections);
        List<SectionWithVotesCount> result = sectionRepositoryMock.findAllWithVotesCount(userId);
        assertNotNull(result);
        assertEquals(sections.size(), result.size());
    }
}
