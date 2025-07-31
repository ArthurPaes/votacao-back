package com.example.pautachallenge.controller;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.pautachallenge.domain.dto.SectionDTO;
import com.example.pautachallenge.domain.interfaces.SectionWithVotesCount;
import com.example.pautachallenge.domain.model.Section;
import com.example.pautachallenge.service.SectionService;

@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/section")
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;

    @GetMapping
    public List<SectionWithVotesCount> getAllSections(@RequestParam Long userId) {
        log.info("Recebendo requisição para buscar todas as seções para o usuário: {}", userId);
        List<SectionWithVotesCount> sections = sectionService.getAllSectionsWithVotes(userId);
        log.info("Retornando {} seções para o usuário: {}", sections.size(), userId);
        return sections;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Section createSection(@Valid @RequestBody SectionDTO sectionDTO) {
        log.info("Recebendo requisição para criar nova seção: {}", sectionDTO.getName());
        
        Section section = new Section();
        section.setName(sectionDTO.getName());
        section.setDescription(sectionDTO.getDescription());
        section.setExpiration(sectionDTO.getExpiration());
        
        Section createdSection = sectionService.createSection(section);
        log.info("Seção criada com sucesso. ID: {}, Nome: {}", createdSection.getId(), createdSection.getName());
        return createdSection;
    }
}
