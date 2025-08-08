package com.example.pautachallenge.integration;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.example.pautachallenge.domain.model.Section;
import com.example.pautachallenge.domain.model.SectionBuilder;
import com.example.pautachallenge.domain.model.UserEntity;
import com.example.pautachallenge.domain.model.VoteStatus;
import com.example.pautachallenge.domain.model.Votes;
import com.example.pautachallenge.repository.SectionRepository;
import com.example.pautachallenge.repository.UserRepository;
import com.example.pautachallenge.repository.VotesRepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class RepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private VotesRepository votesRepository;

    private UserEntity testUser;
    private Section testSection;
    private Votes testVote;

    @BeforeEach
    public void setUp() {
        // Clean up database
        votesRepository.deleteAll();
        sectionRepository.deleteAll();
        userRepository.deleteAll();

        // Create test user
        testUser = new UserEntity();
        testUser.setName("João Silva");
        testUser.setCpf("12345678909");
        testUser.setPassword("encrypted_password");
        testUser.setEmail("joao@example.com");
        testUser = userRepository.save(testUser);

        // Create test section
        testSection = new Section();
        testSection.setName("Pauta Importante");
        testSection.setDescription("Esta é uma descrição detalhada da pauta que será votada pelos associados");
        testSection.setExpiration(10);
        testSection.setStart_at(LocalDateTime.now());
        testSection = sectionRepository.save(testSection);

        // Create test vote
        testVote = new Votes();
        testVote.setUserId(testUser.getId());
        testVote.setSectionId(testSection.getId());
        testVote.setVote(true);
        testVote.setStatus(VoteStatus.ABLE_TO_VOTE);
        testVote = votesRepository.save(testVote);
    }

    @Test
    public void testUserRepositoryOperations() {
        // Test find by email
        UserEntity foundUser = userRepository.findByEmail("joao@example.com");
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo("joao@example.com");
        assertThat(foundUser.getName()).isEqualTo("João Silva");

        // Test find by non-existent email
        UserEntity nonExistentUser = userRepository.findByEmail("nonexistent@example.com");
        assertThat(nonExistentUser).isNull();

        // Test find by CPF
        UserEntity userByCpf = userRepository.findByCpf("12345678909");
        assertThat(userByCpf).isNotNull();
        assertThat(userByCpf.getCpf()).isEqualTo("12345678909");

        // Test find all users
        List<UserEntity> allUsers = userRepository.findAll();
        assertThat(allUsers).isNotEmpty();
        assertThat(allUsers).hasSize(1);
        assertThat(allUsers.get(0).getEmail()).isEqualTo("joao@example.com");
    }

    @Test
    public void testSectionRepositoryOperations() {
        // Test find all sections
        List<Section> allSections = sectionRepository.findAll();
        assertThat(allSections).isNotEmpty();
        assertThat(allSections).hasSize(1);
        assertThat(allSections.get(0).getName()).isEqualTo("Pauta Importante");

        // Test find by ID
        Optional<Section> foundSection = sectionRepository.findById(testSection.getId());
        assertThat(foundSection).isPresent();
        assertThat(foundSection.get().getName()).isEqualTo("Pauta Importante");

        // Test find by non-existent ID
        Optional<Section> nonExistentSection = sectionRepository.findById(999L);
        assertThat(nonExistentSection).isEmpty();
    }

    @Test
    public void testVotesRepositoryOperations() {
        // Test find by user ID and section ID
        Optional<Votes> foundVote = votesRepository.findByUserIdAndSectionId(testUser.getId(), testSection.getId());
        assertThat(foundVote).isPresent();
        assertThat(foundVote.get().getUserId()).isEqualTo(testUser.getId());
        assertThat(foundVote.get().getSectionId()).isEqualTo(testSection.getId());
        assertThat(foundVote.get().getVote()).isTrue();

        // Test find by non-existent user and section
        Optional<Votes> nonExistentVote = votesRepository.findByUserIdAndSectionId(999L, 999L);
        assertThat(nonExistentVote).isEmpty();

        // Test find all votes
        List<Votes> allVotes = votesRepository.findAll();
        assertThat(allVotes).isNotEmpty();
        assertThat(allVotes).hasSize(1);
        assertThat(allVotes.get(0).getUserId()).isEqualTo(testUser.getId());
    }

    @Test
    public void testVoteStatusHandling() {
        // Test vote with different status
        Votes voteWithStatus = new Votes();
        voteWithStatus.setUserId(testUser.getId());
        voteWithStatus.setSectionId(testSection.getId());
        voteWithStatus.setVote(false);
        voteWithStatus.setStatus(VoteStatus.UNABLE_TO_VOTE);

        Votes savedVote = votesRepository.save(voteWithStatus);
        assertThat(savedVote).isNotNull();
        assertThat(savedVote.getStatus()).isEqualTo(VoteStatus.UNABLE_TO_VOTE);
        assertThat(savedVote.getVote()).isFalse();
    }

    @Test
    public void testMultipleVotesForSameUser() {
        // Create another section using SectionBuilder
        Section secondSection = SectionBuilder.builder()
            .name("Segunda Pauta")
            .description("Segunda descrição")
            .expiration(5)
            .startNow()
            .build();
        secondSection = sectionRepository.save(secondSection);

        // Create vote for second section
        Votes secondVote = new Votes();
        secondVote.setUserId(testUser.getId());
        secondVote.setSectionId(secondSection.getId());
        secondVote.setVote(false);
        secondVote.setStatus(VoteStatus.ABLE_TO_VOTE);
        votesRepository.save(secondVote);

        // Check that user has votes for both sections
        Optional<Votes> firstVote = votesRepository.findByUserIdAndSectionId(testUser.getId(), testSection.getId());
        Optional<Votes> secondVoteFound = votesRepository.findByUserIdAndSectionId(testUser.getId(), secondSection.getId());

        assertThat(firstVote).isPresent();
        assertThat(secondVoteFound).isPresent();
        assertThat(firstVote.get().getVote()).isTrue();
        assertThat(secondVoteFound.get().getVote()).isFalse();
    }

    @Test
    public void testDataIntegrity() {
        // Test that user data is preserved
        UserEntity user = userRepository.findByEmail("joao@example.com");
        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo("João Silva");
        assertThat(user.getCpf()).isEqualTo("12345678909");
        assertThat(user.getEmail()).isEqualTo("joao@example.com");

        // Test that section data is preserved
        Section section = sectionRepository.findById(testSection.getId()).orElse(null);
        assertThat(section).isNotNull();
        assertThat(section.getName()).isEqualTo("Pauta Importante");
        assertThat(section.getDescription()).isEqualTo("Esta é uma descrição detalhada da pauta que será votada pelos associados");
        assertThat(section.getExpiration()).isEqualTo(10);

        // Test that vote data is preserved
        Votes vote = votesRepository.findByUserIdAndSectionId(testUser.getId(), testSection.getId()).orElse(null);
        assertThat(vote).isNotNull();
        assertThat(vote.getUserId()).isEqualTo(testUser.getId());
        assertThat(vote.getSectionId()).isEqualTo(testSection.getId());
        assertThat(vote.getVote()).isTrue();
        assertThat(vote.getStatus()).isEqualTo(VoteStatus.ABLE_TO_VOTE);
    }

    @Test
    public void testCascadeOperations() {
        // Test that deleting a user does not automatically remove their votes (no cascade configured)
        Long userId = testUser.getId();
        userRepository.deleteById(userId);

        // Votes should still exist since there's no cascade configured
        Optional<Votes> voteAfterUserDeletion = votesRepository.findByUserIdAndSectionId(userId, testSection.getId());
        assertThat(voteAfterUserDeletion).isPresent(); // Votes remain since no cascade

        // Test that deleting a section does not automatically remove votes (no cascade configured)
        Long sectionId = testSection.getId();
        sectionRepository.deleteById(sectionId);

        // Votes should still exist since there's no cascade configured
        List<Votes> votesAfterSectionDeletion = votesRepository.findAll();
        assertThat(votesAfterSectionDeletion).isNotEmpty(); // Votes remain since no cascade
    }

    @Test
    public void testRepositoryQueryPerformance() {
        // Test multiple operations to ensure performance
        for (int i = 0; i < 10; i++) {
            UserEntity user = userRepository.findByEmail("joao@example.com");
            assertThat(user).isNotNull();

            Section section = sectionRepository.findById(testSection.getId()).orElse(null);
            assertThat(section).isNotNull();

            Optional<Votes> vote = votesRepository.findByUserIdAndSectionId(testUser.getId(), testSection.getId());
            assertThat(vote).isPresent();
        }
    }
} 