package com.sicredi.pautachallenge.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sicredi.pautachallenge.domain.interfaces.SectionWithVotesCount;
import com.sicredi.pautachallenge.domain.model.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {
    @Query(value = "SELECT s.*, " +
            "(SELECT COUNT(v.id) FROM votes v WHERE v.section_id = s.id) as totalVotes, " +
            "(SELECT COUNT(v.id) FROM votes v WHERE v.section_id = s.id AND v.vote = true) as votesTrue, " +
            "(SELECT COUNT(v.id) FROM votes v WHERE v.section_id = s.id AND v.vote = false) as votesFalse, " +
            "(CASE WHEN EXISTS (SELECT 1 FROM votes v WHERE v.section_id = s.id AND v.user_id = :user_id) THEN true ELSE false END) as hasVoted, "
            +
            "(CASE WHEN NOW() > DATEADD('MINUTE', s.expiration, s.start_at) THEN true ELSE false END) as isExpired "
            +
            "FROM sections s", nativeQuery = true)
    List<SectionWithVotesCount> findAllWithVotesCount(@Param("user_id") Long user_id);
}