package com.example.pautachallenge.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pautachallenge.domain.interfaces.UserLoginRequest;
import com.example.pautachallenge.domain.dto.UserResponseDTO;
import com.example.pautachallenge.service.UserService;

@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping
    public UserResponseDTO login(@Valid @RequestBody UserLoginRequest userBody) {
        log.info("Tentativa de login para o email: {}", userBody.getEmail());
        userService.authenticate(userBody.getEmail(), userBody.getPassword());
        log.info("Login realizado com sucesso para o email: {}", userBody.getEmail());
        UserResponseDTO user = userService.findUser(userBody.getEmail());
        log.debug("Retornando dados do usuário: {}", user.getEmail());
        return user;
    }
}
