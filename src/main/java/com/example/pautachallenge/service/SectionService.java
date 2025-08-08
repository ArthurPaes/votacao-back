package com.example.pautachallenge.service;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.example.pautachallenge.domain.dto.SectionDTO;
import com.example.pautachallenge.domain.interfaces.SectionWithVotesCount;
import com.example.pautachallenge.domain.model.Section;
import com.example.pautachallenge.domain.model.SectionBuilder;
import com.example.pautachallenge.repository.SectionRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;

    public List<SectionWithVotesCount> getAllSectionsWithVotes(Long userId) {
        log.debug("Buscando todas as seções com contagem de votos para o usuário: {}", userId);
        List<SectionWithVotesCount> sections = sectionRepository.findAllWithVotesCount(userId);
        log.debug("Encontradas {} seções para o usuário: {}", sections.size(), userId);
        return sections;
    }

    public Section createSection(SectionDTO sectionDTO) {
        log.debug("Criando nova seção: {}", sectionDTO.name());
        
        Section section = createSectionFromDTO(sectionDTO);
        Section savedSection = sectionRepository.save(section);
        
        log.debug("Seção salva com sucesso. ID: {}, Nome: {}", savedSection.getId(), savedSection.getName());
        return savedSection;
    }

    private Section createSectionFromDTO(SectionDTO sectionDTO) {
        return SectionBuilder.builder()
            .name(sectionDTO.name())
            .description(sectionDTO.description())
            .expiration(sectionDTO.expiration())
            .startNow()
            .build();
    }
}
