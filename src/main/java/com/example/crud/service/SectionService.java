package com.example.crud.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.crud.domain.interfaces.SectionWithVotesCount;
import com.example.crud.domain.model.Section;
import com.example.crud.repository.SectionRepository;

@Service
public class SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    public List<SectionWithVotesCount> getAllSectionsWithVotes(Long userId) {
        return sectionRepository.findAllWithVotesCount(userId);
    }

    public Section createSection(Section section) {
        Section newSection = new Section();
        newSection.setName(section.getName());
        newSection.setDescription(section.getDescription());
        newSection.setExpiration(section.getExpiration());
        newSection.setStart_at(LocalDateTime.now());

        return sectionRepository.save(newSection);
    }
}
