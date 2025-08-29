package com.sicredi.pautachallenge.service;

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

import com.sicredi.pautachallenge.domain.dto.UserDTO;
import com.sicredi.pautachallenge.domain.dto.UserResponseDTO;
import com.sicredi.pautachallenge.domain.model.UserEntity;
import com.sicredi.pautachallenge.exception.AuthenticationException;
import com.sicredi.pautachallenge.infra.mapper.UserMapper;
import com.sicredi.pautachallenge.repository.UserRepository;
import com.sicredi.pautachallenge.utils.BcryptUtils;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
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

        List<UserResponseDTO> expectedResponseDTOs = Arrays.asList(
                new UserResponseDTO(1L, "John Doe 1", "123456789", "test@example.com"),
                new UserResponseDTO(2L, "John Doe 2", "123456789", "test2@example.com"));

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toResponseDTO(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity entity = invocation.getArgument(0);
            if (entity.getId() == 1L) {
                return expectedResponseDTOs.get(0);
            } else {
                return expectedResponseDTOs.get(1);
            }
        });

        List<UserResponseDTO> returnedUsers = userService.getUsers();

        assertEquals(2, returnedUsers.size());
        assertEquals(expectedResponseDTOs.get(0).email(), returnedUsers.get(0).email());
        assertEquals(expectedResponseDTOs.get(1).email(), returnedUsers.get(1).email());
    }

    @Test
    public void testCreateUser() {
        UserDTO userDTO = new UserDTO("John Doe", "123456789", "password", "test@example.com");
        UserEntity userEntity = new UserEntity(1L, "John Doe", "123456789", "encrypted_password", "test@example.com");
        UserResponseDTO expectedResponseDTO = new UserResponseDTO(1L, "John Doe", "123456789", "test@example.com");

        when(userRepository.findByEmail(userDTO.email())).thenReturn(null);
        when(userRepository.findByCpf(userDTO.cpf())).thenReturn(null);
        when(userMapper.toEntity(userDTO)).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userMapper.toResponseDTO(userEntity)).thenReturn(expectedResponseDTO);

        UserResponseDTO createdUser = userService.createUser(userDTO);

        assertEquals(expectedResponseDTO.email(), createdUser.email());
        assertNotNull(createdUser.id());
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

        assertThrows(AuthenticationException.class, () -> userService.authenticate(email, password));
    }

    @Test
    public void testFindUser() {
        String email = "test@example.com";
        UserEntity user = new UserEntity(1L, "John Doe", "123456789", "password", "test@example.com");
        UserResponseDTO expectedResponseDTO = new UserResponseDTO(1L, "John Doe", "123456789", "test@example.com");

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(expectedResponseDTO);

        UserResponseDTO foundUser = userService.findUser(email);

        assertNotNull(foundUser);
        assertEquals(expectedResponseDTO.email(), foundUser.email());
    }

    @Test
    public void testFindUser_NotFound() {
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(null);

        UserResponseDTO foundUser = userService.findUser(email);

        assertNull(foundUser);
    }
}