package com.example.pautachallenge.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.pautachallenge.domain.dto.UserDTO;
import com.example.pautachallenge.domain.dto.UserResponseDTO;
import com.example.pautachallenge.domain.model.UserEntity;
import com.example.pautachallenge.repository.UserRepository;
import com.example.pautachallenge.utils.BcryptUtils;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUsers() {
        List<UserEntity> users = Arrays.asList(
                new UserEntity(1L, "John Doe 1", "123456789", "password", "test@example.com"),
                new UserEntity(2L, "John Doe 2", "123456789", "password", "test2@example.com"));

        when(userRepository.findAll()).thenReturn(users);

        List<UserResponseDTO> returnedUsers = userService.getUsers();

        assertEquals(users.size(), returnedUsers.size());
        assertEquals(users.get(0).getEmail(), returnedUsers.get(0).getEmail());
        assertEquals(users.get(1).getEmail(), returnedUsers.get(1).getEmail());
    }

    @Test
    public void testCreateUser() {
        UserDTO userDTO = new UserDTO("John Doe", "123456789", "password", "test@example.com");
        UserEntity userEntity = new UserEntity(1L, "John Doe", "123456789", "encrypted_password", "test@example.com");

        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(null);
        when(userRepository.findByCpf(userDTO.getCpf())).thenReturn(null);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserResponseDTO createdUser = userService.createUser(userDTO);

        assertEquals(userEntity.getEmail(), createdUser.getEmail());
        assertNotNull(createdUser.getId());
    }

    @Test
    public void testAuthenticate_ValidCredentials() {
        String email = "test@example.com";
        String password = "password";
        UserEntity user = new UserEntity(1L, "John Doe", "123456789", BcryptUtils.encryptPassword(password), "test@example.com");

        when(userRepository.findByEmail(email)).thenReturn(user);

        // Should not throw exception
        assertDoesNotThrow(() -> userService.authenticate(email, password));
    }

    @Test
    public void testAuthenticate_InvalidCredentials() {
        String email = "test@example.com";
        String password = "password";
        UserEntity user = new UserEntity(1L, "John Doe", "123456789", BcryptUtils.encryptPassword("different_password"),
                "test@example.com");

        when(userRepository.findByEmail(email)).thenReturn(user);

        // Should throw exception
        assertThrows(IllegalArgumentException.class, () -> userService.authenticate(email, password));
    }

    @Test
    public void testFindUser() {
        String email = "test@example.com";
        UserEntity user = new UserEntity(1L, "John Doe", "123456789", "password", "test@example.com");

        when(userRepository.findByEmail(email)).thenReturn(user);

        UserResponseDTO foundUser = userService.findUser(email);

        assertNotNull(foundUser);
        assertEquals(email, foundUser.getEmail());
    }

}