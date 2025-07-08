package com.example.crud.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.crud.domain.model.Votes;

public interface VotesRepository extends JpaRepository<Votes, Long> {
    Optional<Votes> findByUserIdAndSectionId(Long userId, Long sectionId);
}