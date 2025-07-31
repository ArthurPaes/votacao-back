package com.example.crud.service;

import com.example.crud.domain.model.User;
import com.example.crud.exception.UserAlreadyExistsException;
import com.example.crud.repository.UserRepository;
import com.example.crud.utils.BcryptUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void createUser(User user) {
        try {
            create(user);
        } catch (IllegalArgumentException e) {
            throw new UserAlreadyExistsException("DUPLICATE_FIELD", e);
        }
    }

    private void create(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        if (userRepository.findByCpf(user.getCpf()) != null) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }
        user.setPassword(BcryptUtils.encryptPassword(user.getPassword()));
        var savedUser = userRepository.save(user);
        log.info("User created with ID: {}", savedUser.getId());
    }

    public Boolean authenticate(String email, String password) {
        User user = userRepository.findByEmail(email);
        return user != null && BcryptUtils.comparePasswords(password, user.getPassword());
    }

    public User findUser(String email) {
        return userRepository.findByEmail(email);
    }
}
