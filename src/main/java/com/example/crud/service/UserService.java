package com.example.crud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.crud.domain.model.User;
import com.example.crud.repository.UserRepository;
import com.example.crud.utils.BcryptUtils;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        if (userRepository.findByCpf(user.getCpf()) != null) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }
        user.setPassword(BcryptUtils.encryptPassword(user.getPassword()));
        return userRepository.save(user);
    }

    public Boolean authenticate(String email, String password) {
        User user = userRepository.findByEmail(email);
        return user != null && BcryptUtils.comparePasswords(password, user.getPassword());
    }

    public User findUser(String email) {
        return userRepository.findByEmail(email);
    }
}
