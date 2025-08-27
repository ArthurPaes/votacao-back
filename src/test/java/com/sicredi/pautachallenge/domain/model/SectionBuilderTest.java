package com.sicredi.pautachallenge.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class SectionBuilderTest {

    @Test
    public void testSectionBuilderBasicUsage() {
        Section section = SectionBuilder.builder()
            .name("Pauta Importante")
            .description("Esta é uma descrição detalhada da pauta")
            .expiration(10)
            .startNow()
            .build();

        assertNotNull(section);
        assertEquals("Pauta Importante", section.getName());
        assertEquals("Esta é uma descrição detalhada da pauta", section.getDescription());
        assertEquals(10, section.getExpiration());
        assertNotNull(section.getStart_at());
    }

    @Test
    public void testSectionBuilderWithCustomStartTime() {
        LocalDateTime customStartTime = LocalDateTime.of(2024, 1, 15, 10, 0, 0);

        Section section = SectionBuilder.builder()
            .name("Pauta com Horário Personalizado")
            .description("Pauta com horário de início específico")
            .expiration(30)
            .startAt(customStartTime)
            .build();

        assertNotNull(section);
        assertEquals("Pauta com Horário Personalizado", section.getName());
        assertEquals(customStartTime, section.getStart_at());
        assertEquals(30, section.getExpiration());
    }

    @Test
    public void testSectionBuilderMethodChaining() {
        Section section = SectionBuilder.builder()
            .name("Pauta Encadeada")
            .description("Testando encadeamento de métodos")
            .expiration(15)
            .startNow()
            .build();

        assertNotNull(section);
        assertEquals("Pauta Encadeada", section.getName());
        assertEquals("Testando encadeamento de métodos", section.getDescription());
        assertEquals(15, section.getExpiration());
    }

    @Test
    public void testSectionBuilderDefaultStartTime() {
        Section section = SectionBuilder.builder()
            .name("Pauta Sem Horário")
            .description("Pauta sem definir horário de início")
            .expiration(5)
            .build();

        assertNotNull(section);
        assertNotNull(section.getStart_at());
        LocalDateTime now = LocalDateTime.now();
        assertTrue(section.getStart_at().isBefore(now.plusSeconds(5)));
        assertTrue(section.getStart_at().isAfter(now.minusSeconds(5)));
    }
} 