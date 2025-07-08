package com.example.crud.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.crud.domain.interfaces.UserLoginRequest;
import com.example.crud.domain.model.User;
import com.example.crud.service.UserService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody UserLoginRequest userBody) {
        boolean isAuthenticated = userService.authenticate(userBody.getEmail(), userBody.getPassword());

        User foundUser = userService.findUser(userBody.getEmail());

        return isAuthenticated
                ? ResponseEntity.ok(foundUser)
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais invalidas!");
    }
}
