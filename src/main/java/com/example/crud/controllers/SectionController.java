package com.example.crud.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.crud.domain.interfaces.SectionWithVotesCount;
import com.example.crud.domain.model.Section;
import com.example.crud.service.SectionService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/section")
public class SectionController {

    @Autowired
    private SectionService sectionService;

    @GetMapping
    public ResponseEntity<List<SectionWithVotesCount>> getAllSections(@RequestParam Long userId) {
        List<SectionWithVotesCount> sections = sectionService.getAllSectionsWithVotes(userId);
        return ResponseEntity.ok(sections);
    }

    @PostMapping
    public ResponseEntity<Section> createSection(@RequestBody Section section) {
        Section createdSection = sectionService.createSection(section);
        return new ResponseEntity<>(createdSection, HttpStatus.CREATED);
    }
}
