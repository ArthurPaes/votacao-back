package com.example.pautachallenge.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.pautachallenge.domain.dto.SectionDTO;
import com.example.pautachallenge.domain.dto.UserDTO;
import com.example.pautachallenge.domain.dto.VoteDTO;
import com.example.pautachallenge.domain.interfaces.UserLoginRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@SpringBootTest
@ActiveProfiles("test")
public class ValidationTests {

    @Autowired
    private Validator validator;

    @Test
    public void testValidUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("João Silva");
        userDTO.setCpf("12345678909"); // Valid CPF format
        userDTO.setPassword("senha123");
        userDTO.setEmail("joao@example.com");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        assertTrue(violations.isEmpty(), "Não deve haver violações para um UserDTO válido");
    }

    @Test
    public void testInvalidUserDTO_EmptyName() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("");
        userDTO.setCpf("12345678909"); // Valid CPF format
        userDTO.setPassword("senha123");
        userDTO.setEmail("joao@example.com");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        assertFalse(violations.isEmpty(), "Deve haver violações para nome vazio");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    public void testInvalidUserDTO_InvalidCPF() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("João Silva");
        userDTO.setCpf("123"); // CPF inválido
        userDTO.setPassword("senha123");
        userDTO.setEmail("joao@example.com");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        assertFalse(violations.isEmpty(), "Deve haver violações para CPF inválido");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("cpf")));
    }

    @Test
    public void testInvalidUserDTO_InvalidEmail() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("João Silva");
        userDTO.setCpf("12345678909"); // Valid CPF format
        userDTO.setPassword("senha123");
        userDTO.setEmail("email-invalido"); // Email inválido

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        assertFalse(violations.isEmpty(), "Deve haver violações para email inválido");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    public void testValidSectionDTO() {
        SectionDTO sectionDTO = new SectionDTO();
        sectionDTO.setName("Pauta Importante");
        sectionDTO.setDescription("Esta é uma descrição detalhada da pauta que será votada pelos associados");
        sectionDTO.setExpiration(10);

        Set<ConstraintViolation<SectionDTO>> violations = validator.validate(sectionDTO);
        assertTrue(violations.isEmpty(), "Não deve haver violações para um SectionDTO válido");
    }

    @Test
    public void testInvalidSectionDTO_ShortName() {
        SectionDTO sectionDTO = new SectionDTO();
        sectionDTO.setName("AB"); // Nome muito curto
        sectionDTO.setDescription("Esta é uma descrição detalhada da pauta que será votada pelos associados");
        sectionDTO.setExpiration(10);

        Set<ConstraintViolation<SectionDTO>> violations = validator.validate(sectionDTO);
        assertFalse(violations.isEmpty(), "Deve haver violações para nome muito curto");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    public void testInvalidSectionDTO_ShortDescription() {
        SectionDTO sectionDTO = new SectionDTO();
        sectionDTO.setName("Pauta Importante");
        sectionDTO.setDescription("Curta"); // Descrição muito curta
        sectionDTO.setExpiration(10);

        Set<ConstraintViolation<SectionDTO>> violations = validator.validate(sectionDTO);
        assertFalse(violations.isEmpty(), "Deve haver violações para descrição muito curta");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    public void testInvalidSectionDTO_InvalidExpiration() {
        SectionDTO sectionDTO = new SectionDTO();
        sectionDTO.setName("Pauta Importante");
        sectionDTO.setDescription("Esta é uma descrição detalhada da pauta que será votada pelos associados");
        sectionDTO.setExpiration(0); // Expiração inválida

        Set<ConstraintViolation<SectionDTO>> violations = validator.validate(sectionDTO);
        assertFalse(violations.isEmpty(), "Deve haver violações para expiração inválida");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("expiration")));
    }

    @Test
    public void testValidVoteDTO() {
        VoteDTO voteDTO = new VoteDTO();
        voteDTO.setSectionId(1L);
        voteDTO.setUserId(1L);
        voteDTO.setVote(true);

        Set<ConstraintViolation<VoteDTO>> violations = validator.validate(voteDTO);
        assertTrue(violations.isEmpty(), "Não deve haver violações para um VoteDTO válido");
    }

    @Test
    public void testInvalidVoteDTO_NullValues() {
        VoteDTO voteDTO = new VoteDTO();
        voteDTO.setSectionId(null);
        voteDTO.setUserId(null);
        voteDTO.setVote(null);

        Set<ConstraintViolation<VoteDTO>> violations = validator.validate(voteDTO);
        assertFalse(violations.isEmpty(), "Deve haver violações para valores nulos");
        assertEquals(3, violations.size(), "Deve haver 3 violações para valores nulos");
    }

    @Test
    public void testValidUserLoginRequest() {
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setEmail("usuario@example.com");
        loginRequest.setPassword("senha123");

        Set<ConstraintViolation<UserLoginRequest>> violations = validator.validate(loginRequest);
        assertTrue(violations.isEmpty(), "Não deve haver violações para um UserLoginRequest válido");
    }

    @Test
    public void testInvalidUserLoginRequest_InvalidEmail() {
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setEmail("email-invalido");
        loginRequest.setPassword("senha123");

        Set<ConstraintViolation<UserLoginRequest>> violations = validator.validate(loginRequest);
        assertFalse(violations.isEmpty(), "Deve haver violações para email inválido");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }
} 