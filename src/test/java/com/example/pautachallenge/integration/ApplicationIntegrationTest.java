package com.example.pautachallenge.integration;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.pautachallenge.domain.dto.SectionDTO;
import com.example.pautachallenge.domain.dto.UserDTO;
import com.example.pautachallenge.domain.dto.VoteDTO;
import com.example.pautachallenge.domain.interfaces.UserLoginRequest;
import com.example.pautachallenge.service.SectionService;
import com.example.pautachallenge.service.UserService;
import com.example.pautachallenge.service.VotesService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
@ActiveProfiles("test")
public class ApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private SectionService sectionService;

    @MockBean
    private VotesService votesService;

    private UserDTO validUserDTO;
    private SectionDTO validSectionDTO;
    private VoteDTO validVoteDTO;

    @BeforeEach
    public void setUp() {
        // Create test data
        validUserDTO = new UserDTO();
        validUserDTO.setName("João Silva");
        validUserDTO.setCpf("12345678909");
        validUserDTO.setPassword("senha123");
        validUserDTO.setEmail("joao@example.com");

        validSectionDTO = new SectionDTO();
        validSectionDTO.setName("Pauta Importante");
        validSectionDTO.setDescription("Esta é uma descrição detalhada da pauta que será votada pelos associados");
        validSectionDTO.setExpiration(10);

        validVoteDTO = new VoteDTO();
        validVoteDTO.setSectionId(1L);
        validVoteDTO.setUserId(1L);
        validVoteDTO.setVote(true);
    }

    @Test
    public void testCompleteUserFlow() throws Exception {
        // This test will be simplified to test the controller layer only
        String userJson = objectMapper.writeValueAsString(validUserDTO);
        
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCompleteSectionFlow() throws Exception {
        String sectionJson = objectMapper.writeValueAsString(validSectionDTO);
        
        mockMvc.perform(post("/section")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sectionJson))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/section")
                .param("userId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCompleteVotingFlow() throws Exception {
        String voteJson = objectMapper.writeValueAsString(validVoteDTO);
        
        mockMvc.perform(post("/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(voteJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void testInvalidUserCreation() throws Exception {
        UserDTO invalidUser = new UserDTO();
        invalidUser.setName("Test User");
        invalidUser.setCpf("12345678909");
        invalidUser.setPassword("password");
        invalidUser.setEmail("invalid-email");

        String invalidUserJson = objectMapper.writeValueAsString(invalidUser);
        
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidUserJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testInvalidSectionCreation() throws Exception {
        SectionDTO invalidSection = new SectionDTO();
        invalidSection.setName("AB"); // Too short
        invalidSection.setDescription("Valid description");
        invalidSection.setExpiration(10);

        String invalidSectionJson = objectMapper.writeValueAsString(invalidSection);
        
        mockMvc.perform(post("/section")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidSectionJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testInvalidVoteData() throws Exception {
        VoteDTO invalidVote = new VoteDTO();
        invalidVote.setSectionId(null);
        invalidVote.setUserId(null);
        invalidVote.setVote(null);

        String invalidVoteJson = objectMapper.writeValueAsString(invalidVote);
        
        mockMvc.perform(post("/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidVoteJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCorsHeaders() throws Exception {
        mockMvc.perform(get("/section")
                .param("userId", "1")
                .header("Origin", "http://localhost:5173"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"));
    }

    @Test
    public void testErrorHandling() throws Exception {
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ invalid json }"))
                .andExpect(status().isBadRequest());
    }
} 