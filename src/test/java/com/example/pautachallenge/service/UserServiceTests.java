package com.example.pautachallenge.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.pautachallenge.domain.dto.UserDTO;
import com.example.pautachallenge.domain.dto.UserResponseDTO;
import com.example.pautachallenge.domain.model.UserEntity;
import com.example.pautachallenge.repository.UserRepository;
import com.example.pautachallenge.utils.BcryptUtils;

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
        UserEntity user1 = new UserEntity();
        user1.setId(1L);
        user1.setName("John Doe 1");
        user1.setCpf("123456789");
        user1.setPassword("password");
        user1.setEmail("test@example.com");
        
        UserEntity user2 = new UserEntity();
        user2.setId(2L);
        user2.setName("John Doe 2");
        user2.setCpf("123456789");
        user2.setPassword("password");
        user2.setEmail("test2@example.com");
        
        List<UserEntity> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        List<UserResponseDTO> returnedUsers = userService.getUsers();

        assertEquals(users.size(), returnedUsers.size());
        assertEquals(users.get(0).getEmail(), returnedUsers.get(0).getEmail());
        assertEquals(users.get(1).getEmail(), returnedUsers.get(1).getEmail());
    }

    @Test
    public void testCreateUser() {
        UserDTO userDTO = new UserDTO("John Doe", "123456789", "password", "test@example.com");
        UserEntity savedUserEntity = new UserEntity();
        savedUserEntity.setId(1L);
        savedUserEntity.setName("John Doe");
        savedUserEntity.setCpf("123456789");
        savedUserEntity.setPassword("encrypted_password");
        savedUserEntity.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(null);
        when(userRepository.findByCpf("123456789")).thenReturn(null);
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUserEntity);

        UserResponseDTO createdUser = userService.createUser(userDTO);

        assertEquals(savedUserEntity.getEmail(), createdUser.getEmail());
        assertNotNull(createdUser.getId());
    }

    @Test
    public void testAuthenticate_ValidCredentials() {
        String email = "test@example.com";
        String password = "password";
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName("John Doe");
        user.setCpf("123456789");
        user.setPassword(BcryptUtils.encryptPassword(password));
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(email)).thenReturn(user);

        // Should not throw exception
        assertDoesNotThrow(() -> userService.authenticate(email, password));
    }

    @Test
    public void testAuthenticate_InvalidCredentials() {
        String email = "test@example.com";
        String password = "password";
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName("John Doe");
        user.setCpf("123456789");
        user.setPassword(BcryptUtils.encryptPassword("different_password"));
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(email)).thenReturn(user);

        // Should throw exception
        assertThrows(IllegalArgumentException.class, () -> userService.authenticate(email, password));
    }

    @Test
    public void testFindUser() {
        String email = "test@example.com";
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName("John Doe");
        user.setCpf("123456789");
        user.setPassword("password");
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(email)).thenReturn(user);

        UserResponseDTO foundUser = userService.findUser(email);

        assertNotNull(foundUser);
        assertEquals(email, foundUser.getEmail());
    }

}