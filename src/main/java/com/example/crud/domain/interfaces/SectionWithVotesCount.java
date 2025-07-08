package com.example.crud.domain.interfaces;

import java.time.LocalDateTime;

public interface SectionWithVotesCount {
    Long getId();
    String getName();
    String getDescription();
    Integer getExpiration();
    LocalDateTime getStart_at();
    Long getTotalVotes();
    Long getVotesTrue();
    Long getVotesFalse();
    Boolean getHasVoted();
    Boolean getIsExpired();
}