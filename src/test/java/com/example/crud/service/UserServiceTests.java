package com.example.crud.service;

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

import com.example.crud.domain.model.User;
import com.example.crud.repository.UserRepository;
import com.example.crud.utils.BcryptUtils;

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
        List<User> users = Arrays.asList(
                new User(1L, "John Doe 1", "123456789", "password", "test@example.com"),
                new User(1L, "John Doe 2", "123456789", "password", "test@example.com"));

        when(userRepository.findAll()).thenReturn(users);

        List<User> returnedUsers = userService.getUsers();

        assertEquals(users.size(), returnedUsers.size());
        assertEquals(users.get(0).getEmail(), returnedUsers.get(0).getEmail());
        assertEquals(users.get(1).getEmail(), returnedUsers.get(1).getEmail());
    }

    @Test
    public void testCreateUser() {
        User user = new User(1L, "John Doe", "123456789", "password", "test@example.com");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertEquals(user.getEmail(), createdUser.getEmail());
        assertNotEquals("password", createdUser.getPassword()); // Password should be encrypted
    }

    @Test
    public void testAuthenticate_ValidCredentials() {
        String email = "test@example.com";
        String password = "password";
        User user = new User(1L, "John Doe", "123456789", BcryptUtils.encryptPassword(password), "test@example.com");

        when(userRepository.findByEmail(email)).thenReturn(user);

        boolean isAuthenticated = userService.authenticate(email, password);

        assertTrue(isAuthenticated);
    }

    @Test
    public void testAuthenticate_InvalidCredentials() {
        String email = "test@example.com";
        String password = "password";
        User user = new User(1L, "John Doe", "123456789", BcryptUtils.encryptPassword("different_password"),
                "test@example.com");

        when(userRepository.findByEmail(email)).thenReturn(user);

        boolean isAuthenticated = userService.authenticate(email, password);

        assertFalse(isAuthenticated);
    }

    @Test
    public void testFindUser() {
        String email = "test@example.com";
        User user = new User(1L, "John Doe", "123456789", "password", "test@example.com");

        when(userRepository.findByEmail(email)).thenReturn(user);

        User foundUser = userService.findUser(email);

        assertNotNull(foundUser);
        assertEquals(email, foundUser.getEmail());
    }

}