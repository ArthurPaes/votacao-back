package com.example.crud.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.crud.domain.model.User;

@SpringBootTest
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setName("John Doe");
        user.setCpf("12345678901");
        user.setPassword("password");
        user.setEmail("john.doe@example.com");
        userRepository.save(user);
    }

    @Test
    public void whenFindByEmail_thenReturnUser() {
        User foundUser = userRepository.findByEmail("john.doe@example.com");

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getName()).isEqualTo("John Doe");
        assertThat(foundUser.getCpf()).isEqualTo("12345678901");
        assertThat(foundUser.getPassword()).isEqualTo("password");
        assertThat(foundUser.getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    public void whenFindByEmail_thenReturnNull() {
        User foundUser = userRepository.findByEmail("nonexistent@example.com");

        assertThat(foundUser).isNull();
    }
}
