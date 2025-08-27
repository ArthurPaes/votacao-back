package com.sicredi.pautachallenge.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.sicredi.pautachallenge.domain.dto.SectionDTO;
import com.sicredi.pautachallenge.domain.dto.UserDTO;
import com.sicredi.pautachallenge.domain.dto.VoteDTO;
import com.sicredi.pautachallenge.domain.interfaces.UserLoginRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@SpringBootTest
class ValidationTests {

    @Autowired
    private Validator validator;

    @Test
    public void testValidUserDTO() {
        UserDTO userDTO = new UserDTO("João Silva", "12345678909", "senha123", "joao@example.com");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        assertTrue(violations.isEmpty(), "Não deve haver violações para um UserDTO válido");
    }

    @Test
    public void testInvalidUserDTO_EmptyName() {
        UserDTO userDTO = new UserDTO("", "12345678909", "senha123", "joao@example.com");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        assertFalse(violations.isEmpty(), "Deve haver violações para nome vazio");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    public void testInvalidUserDTO_InvalidCPF() {
        UserDTO userDTO = new UserDTO("João Silva", "123", "senha123", "joao@example.com");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        assertFalse(violations.isEmpty(), "Deve haver violações para CPF inválido");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("cpf")));
    }

    @Test
    public void testInvalidUserDTO_InvalidEmail() {
        UserDTO userDTO = new UserDTO("João Silva", "12345678909", "senha123", "email-invalido");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        assertFalse(violations.isEmpty(), "Deve haver violações para email inválido");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    public void testValidSectionDTO() {
        SectionDTO sectionDTO = new SectionDTO("Pauta Importante", "Esta é uma descrição detalhada da pauta que será votada pelos associados", 10);

        Set<ConstraintViolation<SectionDTO>> violations = validator.validate(sectionDTO);
        assertTrue(violations.isEmpty(), "Não deve haver violações para um SectionDTO válido");
    }

    @Test
    public void testInvalidSectionDTO_ShortName() {
        SectionDTO sectionDTO = new SectionDTO("AB", "Esta é uma descrição detalhada da pauta que será votada pelos associados", 10);

        Set<ConstraintViolation<SectionDTO>> violations = validator.validate(sectionDTO);
        assertFalse(violations.isEmpty(), "Deve haver violações para nome muito curto");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    public void testInvalidSectionDTO_ShortDescription() {
        SectionDTO sectionDTO = new SectionDTO("Pauta Importante", "Curta", 10);

        Set<ConstraintViolation<SectionDTO>> violations = validator.validate(sectionDTO);
        assertFalse(violations.isEmpty(), "Deve haver violações para descrição muito curta");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    public void testInvalidSectionDTO_InvalidExpiration() {
        SectionDTO sectionDTO = new SectionDTO("Pauta Importante", "Esta é uma descrição detalhada da pauta que será votada pelos associados", 0);

        Set<ConstraintViolation<SectionDTO>> violations = validator.validate(sectionDTO);
        assertFalse(violations.isEmpty(), "Deve haver violações para expiração inválida");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("expiration")));
    }

    @Test
    public void testValidVoteDTO() {
        VoteDTO voteDTO = new VoteDTO(1L, 1L, true);

        Set<ConstraintViolation<VoteDTO>> violations = validator.validate(voteDTO);
        assertTrue(violations.isEmpty(), "Não deve haver violações para um VoteDTO válido");
    }

    @Test
    public void testInvalidVoteDTO_NullValues() {
        VoteDTO voteDTO = new VoteDTO(null, null, null);

        Set<ConstraintViolation<VoteDTO>> violations = validator.validate(voteDTO);
        assertFalse(violations.isEmpty(), "Deve haver violações para valores nulos");
        assertEquals(3, violations.size(), "Deve haver 3 violações para valores nulos");
    }

    @Test
    public void testValidUserLoginRequest() {
        UserLoginRequest loginRequest = new UserLoginRequest("joao@example.com", "senha123");

        Set<ConstraintViolation<UserLoginRequest>> violations = validator.validate(loginRequest);
        assertTrue(violations.isEmpty(), "Não deve haver violações para um UserLoginRequest válido");
    }

    @Test
    public void testInvalidUserLoginRequest_InvalidEmail() {
        UserLoginRequest loginRequest = new UserLoginRequest("email-invalido", "senha123");

        Set<ConstraintViolation<UserLoginRequest>> violations = validator.validate(loginRequest);
        assertFalse(violations.isEmpty(), "Deve haver violações para email inválido");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }
} 