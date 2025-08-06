package com.example.pautachallenge.integration;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.example.pautachallenge.domain.dto.SectionDTO;
import com.example.pautachallenge.domain.dto.UserDTO;
import com.example.pautachallenge.domain.dto.UserResponseDTO;
import com.example.pautachallenge.domain.interfaces.SectionWithVotesCount;
import com.example.pautachallenge.domain.model.Section;
import com.example.pautachallenge.domain.model.VoteStatus;
import com.example.pautachallenge.domain.model.Votes;
import com.example.pautachallenge.service.SectionService;
import com.example.pautachallenge.service.UserService;
import com.example.pautachallenge.service.VotesService;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private VotesService votesService;

    private UserDTO validUserDTO;
    private Section validSection;

    @BeforeEach
    public void setUp() {
        validUserDTO = new UserDTO();
        validUserDTO.setName("João Silva");
        validUserDTO.setCpf("12345678909");
        validUserDTO.setPassword("senha123");
        validUserDTO.setEmail("joao@example.com");

        validSection = new Section();
        validSection.setName("Pauta Importante");
        validSection.setDescription("Esta é uma descrição detalhada da pauta que será votada pelos associados");
        validSection.setExpiration(10);
        validSection.setStart_at(LocalDateTime.now());
    }

    @Test
    public void testUserServiceIntegration() {
        // Test user creation
        UserResponseDTO createdUser = userService.createUser(validUserDTO);
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getEmail()).isEqualTo("joao@example.com");
        assertThat(createdUser.getName()).isEqualTo("João Silva");
        assertThat(createdUser.getId()).isNotNull();

        // Test user authentication
        assertThatCode(() -> userService.authenticate("joao@example.com", "senha123"))
                .doesNotThrowAnyException();

        // Test user find
        UserResponseDTO foundUser = userService.findUser("joao@example.com");
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo("joao@example.com");

        // Test get all users
        List<UserResponseDTO> allUsers = userService.getUsers();
        assertThat(allUsers).isNotEmpty();
        assertThat(allUsers).anyMatch(user -> user.getEmail().equals("joao@example.com"));
    }

    @Test
    public void testSectionServiceIntegration() {
        // Test section creation
        Section createdSection = sectionService.createSection(validSection);
        assertThat(createdSection).isNotNull();
        assertThat(createdSection.getId()).isNotNull();
        assertThat(createdSection.getName()).isEqualTo("Pauta Importante");

        // Test get sections with votes count
        List<SectionWithVotesCount> sectionsWithVotes = sectionService.getAllSectionsWithVotes(1L);
        assertThat(sectionsWithVotes).isNotEmpty();
        assertThat(sectionsWithVotes.get(0).getName()).isEqualTo("Pauta Importante");
        assertThat(sectionsWithVotes.get(0).getTotalVotes()).isEqualTo(0L);
    }

    @Test
    public void testVotesServiceIntegration() {
        // Create user and section first
        UserResponseDTO user = userService.createUser(validUserDTO);
        Section section = sectionService.createSection(validSection);

        // Test vote creation
        Votes vote = new Votes();
        vote.setUserId(user.getId());
        vote.setSectionId(section.getId());
        vote.setVote(true);

        Votes createdVote = votesService.createVote(vote);
        assertThat(createdVote).isNotNull();
        assertThat(createdVote.getId()).isNotNull();
        assertThat(createdVote.getUserId()).isEqualTo(user.getId());
        assertThat(createdVote.getSectionId()).isEqualTo(section.getId());
        assertThat(createdVote.getVote()).isTrue();

        // Test duplicate vote prevention
        Votes duplicateVote = new Votes();
        duplicateVote.setUserId(user.getId());
        duplicateVote.setSectionId(section.getId());
        duplicateVote.setVote(false);

        Votes duplicateResult = votesService.createVote(duplicateVote);
        assertThat(duplicateResult).isNull();
    }

    @Test
    public void testCompleteVotingWorkflow() {
        // 1. Create user
        UserResponseDTO user = userService.createUser(validUserDTO);
        assertThat(user).isNotNull();

        // 2. Create section
        Section section = sectionService.createSection(validSection);
        assertThat(section).isNotNull();

        // 3. Vote
        Votes vote = new Votes();
        vote.setUserId(user.getId());
        vote.setSectionId(section.getId());
        vote.setVote(true);

        Votes createdVote = votesService.createVote(vote);
        assertThat(createdVote).isNotNull();

        // 4. Check sections with vote count
        List<SectionWithVotesCount> sectionsWithVotes = sectionService.getAllSectionsWithVotes(user.getId());
        assertThat(sectionsWithVotes).isNotEmpty();
        assertThat(sectionsWithVotes.get(0).getTotalVotes()).isEqualTo(1L);
    }

    @Test
    public void testUserAuthenticationFailure() {
        // Create user
        userService.createUser(validUserDTO);

        // Test wrong password
        assertThatThrownBy(() -> userService.authenticate("joao@example.com", "wrongpassword"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Credenciais inválidas");
    }

    @Test
    public void testUserNotFound() {
        // Test authentication with non-existent user
        assertThatThrownBy(() -> userService.authenticate("nonexistent@example.com", "password"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Credenciais inválidas");
    }

    @Test
    public void testDuplicateUserCreation() {
        // Create user first time
        userService.createUser(validUserDTO);

        // Try to create same user again
        assertThatThrownBy(() -> userService.createUser(validUserDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email já cadastrado");
    }

    @Test
    public void testVoteStatusHandling() {
        // Create user and section
        UserResponseDTO user = userService.createUser(validUserDTO);
        Section section = sectionService.createSection(validSection);

        // Create vote
        Votes vote = new Votes();
        vote.setUserId(user.getId());
        vote.setSectionId(section.getId());
        vote.setVote(true);
        vote.setStatus(VoteStatus.ABLE_TO_VOTE);

        Votes createdVote = votesService.createVote(vote);
        assertThat(createdVote).isNotNull();
        assertThat(createdVote.getStatus()).isEqualTo(VoteStatus.ABLE_TO_VOTE);
    }

    @Test
    public void testSectionWithMultipleVotes() {
        // Create user and section
        UserResponseDTO user = userService.createUser(validUserDTO);
        Section section = sectionService.createSection(validSection);

        // Create multiple votes (should only allow one per user)
        Votes vote1 = new Votes();
        vote1.setUserId(user.getId());
        vote1.setSectionId(section.getId());
        vote1.setVote(true);

        Votes vote2 = new Votes();
        vote2.setUserId(user.getId());
        vote2.setSectionId(section.getId());
        vote2.setVote(false);

        // First vote should succeed
        Votes createdVote1 = votesService.createVote(vote1);
        assertThat(createdVote1).isNotNull();

        // Second vote should fail (duplicate)
        Votes createdVote2 = votesService.createVote(vote2);
        assertThat(createdVote2).isNull();
    }

    @Test
    public void testUserServiceWithInvalidData() {
        // Test with invalid email
        UserDTO invalidUser = new UserDTO();
        invalidUser.setName("Test User");
        invalidUser.setCpf("12345678909");
        invalidUser.setPassword("password");
        invalidUser.setEmail("invalid-email");

        assertThatThrownBy(() -> userService.createUser(invalidUser))
                .isInstanceOf(IllegalArgumentException.class);
    }
} 