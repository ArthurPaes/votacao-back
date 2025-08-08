package com.example.pautachallenge.infra.mapper;

import com.example.pautachallenge.domain.dto.UserDTO;
import com.example.pautachallenge.domain.dto.UserResponseDTO;
import com.example.pautachallenge.domain.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    public void testToEntity() {
        UserDTO userDTO = new UserDTO("João Silva", "12345678901", "senha123", "joao@example.com");

        UserEntity userEntity = userMapper.toEntity(userDTO);

        assertNotNull(userEntity);
        assertEquals("João Silva", userEntity.getName());
        assertEquals("12345678901", userEntity.getCpf());
        assertEquals("senha123", userEntity.getPassword());
        assertEquals("joao@example.com", userEntity.getEmail());
    }

    @Test
    public void testToResponseDTO() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("Maria Santos");
        userEntity.setCpf("98765432100");
        userEntity.setPassword("senha456");
        userEntity.setEmail("maria@example.com");

        UserResponseDTO userResponseDTO = userMapper.toResponseDTO(userEntity);

        assertNotNull(userResponseDTO);
        assertEquals(1L, userResponseDTO.id());
        assertEquals("Maria Santos", userResponseDTO.name());
        assertEquals("98765432100", userResponseDTO.cpf());
        assertEquals("maria@example.com", userResponseDTO.email());
    }

    @Test
    public void testToDTO() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("Pedro Costa");
        userEntity.setCpf("11122233344");
        userEntity.setPassword("senha789");
        userEntity.setEmail("pedro@example.com");

        UserDTO userDTO = userMapper.toDTO(userEntity);

        assertNotNull(userDTO);
        assertEquals("Pedro Costa", userDTO.name());
        assertEquals("11122233344", userDTO.cpf());
        assertEquals("senha789", userDTO.password());
        assertEquals("pedro@example.com", userDTO.email());
    }

    @Test
    public void testUpdateEntityFromDTO() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("Nome Original");
        userEntity.setCpf("11111111111");
        userEntity.setPassword("senha_original");
        userEntity.setEmail("original@example.com");

        UserDTO userDTO = new UserDTO("Nome Atualizado", "22222222222", "senha_atualizada", "atualizado@example.com");

        UserEntity updatedEntity = userMapper.updateEntityFromDTO(userEntity, userDTO);

        assertNotNull(updatedEntity);
        assertEquals(1L, updatedEntity.getId());
        assertEquals("Nome Atualizado", updatedEntity.getName());
        assertEquals("22222222222", updatedEntity.getCpf());
        assertEquals("senha_atualizada", updatedEntity.getPassword());
        assertEquals("atualizado@example.com", updatedEntity.getEmail());
    }

    @Test
    public void testToEntityWithNullValues() {
        UserDTO userDTO = new UserDTO(null, null, null, null);

        UserEntity userEntity = userMapper.toEntity(userDTO);

        assertNotNull(userEntity);
        assertNull(userEntity.getName());
        assertNull(userEntity.getCpf());
        assertNull(userEntity.getPassword());
        assertNull(userEntity.getEmail());
    }

    @Test
    public void testToResponseDTOWithNullValues() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName(null);
        userEntity.setCpf(null);
        userEntity.setPassword("senha");
        userEntity.setEmail(null);

        UserResponseDTO userResponseDTO = userMapper.toResponseDTO(userEntity);

        assertNotNull(userResponseDTO);
        assertEquals(1L, userResponseDTO.id());
        assertNull(userResponseDTO.name());
        assertNull(userResponseDTO.cpf());
        assertNull(userResponseDTO.email());
    }

    @Test
    public void testToDTOWithNullValues() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName(null);
        userEntity.setCpf(null);
        userEntity.setPassword(null);
        userEntity.setEmail(null);

        UserDTO userDTO = userMapper.toDTO(userEntity);

        assertNotNull(userDTO);
        assertNull(userDTO.name());
        assertNull(userDTO.cpf());
        assertNull(userDTO.password());
        assertNull(userDTO.email());
    }
} 