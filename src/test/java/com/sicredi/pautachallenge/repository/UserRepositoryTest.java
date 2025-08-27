package com.sicredi.pautachallenge.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.sicredi.pautachallenge.domain.model.UserEntity;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveUser() {
        UserEntity user = new UserEntity();
        user.setName("Test User");
        user.setCpf("12345678909");
        user.setPassword("password123");
        user.setEmail("test@example.com");

        UserEntity savedUser = userRepository.save(user);
        
        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals("Test User", savedUser.getName());
        assertEquals("12345678909", savedUser.getCpf());
        assertEquals("test@example.com", savedUser.getEmail());
    }

    @Test
    public void testFindByEmail() {
        UserEntity user = new UserEntity();
        user.setName("Test User");
        user.setCpf("12345678909");
        user.setPassword("password123");
        user.setEmail("test@example.com");

        userRepository.save(user);
        
        UserEntity foundUser = userRepository.findByEmail("test@example.com");
        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
    }

    @Test
    public void testFindByCpf() {
        UserEntity user = new UserEntity();
        user.setName("Test User");
        user.setCpf("12345678909");
        user.setPassword("password123");
        user.setEmail("test@example.com");

        userRepository.save(user);
        
        UserEntity foundUser = userRepository.findByCpf("12345678909");
        assertNotNull(foundUser);
        assertEquals("12345678909", foundUser.getCpf());
    }

    @Test
    public void testFindAll() {
        List<UserEntity> users = userRepository.findAll();
        assertNotNull(users);
        // Como não há dados no banco de teste, esperamos uma lista vazia
        assertEquals(0, users.size());
    }

    @Test
    public void testFindByEmailNotFound() {
        UserEntity foundUser = userRepository.findByEmail("nonexistent@example.com");
        assertNull(foundUser);
    }

    @Test
    public void testFindByCpfNotFound() {
        UserEntity foundUser = userRepository.findByCpf("00000000000");
        assertNull(foundUser);
    }
} 