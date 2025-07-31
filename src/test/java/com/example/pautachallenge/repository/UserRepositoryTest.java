package com.example.pautachallenge.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.pautachallenge.domain.model.UserEntity;

@SpringBootTest
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        UserEntity user = new UserEntity();
        user.setName("John Doe");
        user.setCpf("12345678901");
        user.setPassword("password");
        user.setEmail("john.doe@example.com");
        userRepository.save(user);
    }

    @Test
    public void whenFindByEmail_thenReturnUser() {
        UserEntity foundUser = userRepository.findByEmail("john.doe@example.com");

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getName()).isEqualTo("John Doe");
        assertThat(foundUser.getCpf()).isEqualTo("12345678901");
        assertThat(foundUser.getPassword()).isEqualTo("password");
        assertThat(foundUser.getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    public void whenFindByEmail_thenReturnNull() {
        UserEntity foundUser = userRepository.findByEmail("nonexistent@example.com");

        assertThat(foundUser).isNull();
    }
}
