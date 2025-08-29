package com.sicredi.pautachallenge.integration;

import com.sicredi.pautachallenge.domain.dto.UserDTO;
import com.sicredi.pautachallenge.domain.dto.SectionDTO;
import com.sicredi.pautachallenge.domain.dto.VoteDTO;
import com.sicredi.pautachallenge.domain.interfaces.UserLoginRequest;
import com.sicredi.pautachallenge.exception.AuthenticationException;
import com.sicredi.pautachallenge.service.UserService;
import com.sicredi.pautachallenge.service.SectionService;
import com.sicredi.pautachallenge.service.VotesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        validUserDTO = new UserDTO(
            "João Silva",
            "12345678909",
            "senha123",
            "joao@example.com"
        );

        validSectionDTO = new SectionDTO(
            "Pauta Importante",
            "Esta é uma descrição detalhada da pauta que será votada pelos associados",
            10
        );

        validVoteDTO = new VoteDTO(1L, 1L, true);
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
        UserDTO invalidUser = new UserDTO(
            "Test User",
            "12345678909",
            "password",
            "invalid-email"
        );

        String invalidUserJson = objectMapper.writeValueAsString(invalidUser);
        
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidUserJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testInvalidSectionCreation() throws Exception {
        SectionDTO invalidSection = new SectionDTO(
            "AB", // Too short
            "Valid description",
            10
        );

        String invalidSectionJson = objectMapper.writeValueAsString(invalidSection);
        
        mockMvc.perform(post("/section")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidSectionJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testInvalidVoteData() throws Exception {
        VoteDTO invalidVote = new VoteDTO(null, null, null);

        String invalidVoteJson = objectMapper.writeValueAsString(invalidVote);
        
        mockMvc.perform(post("/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidVoteJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCorsHeaders() throws Exception {
        mockMvc.perform(options("/user")
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "POST")
                .header("Access-Control-Request-Headers", "Content-Type"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "*"))
                .andExpect(header().string("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS,HEAD,TRACE,CONNECT"))
                .andExpect(header().string("Access-Control-Allow-Headers", "Content-Type"));
    }

    @Test
    public void testErrorHandling() throws Exception {
        mockMvc.perform(get("/nonexistent-endpoint"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAuthenticationFlow() throws Exception {
        // Test successful authentication
        String authRequest = "{\"email\":\"test@example.com\",\"password\":\"password123\"}";
        
        mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authRequest))
                .andExpect(status().isOk());
    }

    @Test
    public void testAuthenticationWithInvalidCredentials() throws Exception {
        // Test authentication with invalid credentials
        String invalidAuthRequest = "{\"email\":\"invalid@example.com\",\"password\":\"wrongpassword\"}";
        
        // Configure mock to throw AuthenticationException for invalid credentials
        when(userService.login(any(UserLoginRequest.class)))
            .thenThrow(new AuthenticationException("Credenciais inválidas!"));
        
        mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidAuthRequest))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testAuthenticationWithInvalidData() throws Exception {
        // Test authentication with invalid data format
        String invalidDataRequest = "{\"email\":\"invalid-email\",\"password\":\"\"}";
        
        mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidDataRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSectionWithInvalidExpiration() throws Exception {
        // Test section creation with invalid expiration time
        SectionDTO invalidSection = new SectionDTO(
            "Valid Name",
            "Valid description",
            -5 // Invalid negative expiration
        );

        String invalidSectionJson = objectMapper.writeValueAsString(invalidSection);
        
        mockMvc.perform(post("/section")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidSectionJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testVoteWithExpiredSection() throws Exception {
        // Test voting on an expired section
        VoteDTO expiredVote = new VoteDTO(999L, 1L, true); // Non-existent section

        // Configure mock to throw IllegalArgumentException for non-existent/expired section
        when(votesService.createVote(any(VoteDTO.class)))
            .thenThrow(new IllegalArgumentException("Seção não encontrada ou expirada."));

        String expiredVoteJson = objectMapper.writeValueAsString(expiredVote);
        
        mockMvc.perform(post("/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(expiredVoteJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUserWithInvalidCPF() throws Exception {
        // Test user creation with invalid CPF
        UserDTO invalidUser = new UserDTO(
            "Test User",
            "123", // Invalid CPF (too short)
            "password123",
            "test@example.com"
        );

        String invalidUserJson = objectMapper.writeValueAsString(invalidUser);
        
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidUserJson))
                .andExpect(status().isBadRequest());
    }
} 