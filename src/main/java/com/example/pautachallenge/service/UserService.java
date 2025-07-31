package com.example.pautachallenge.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.example.pautachallenge.domain.dto.UserDTO;
import com.example.pautachallenge.domain.dto.UserResponseDTO;
import com.example.pautachallenge.domain.model.UserEntity;
import com.example.pautachallenge.repository.UserRepository;
import com.example.pautachallenge.utils.BcryptUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserResponseDTO> getUsers() {
        log.debug("Buscando todos os usuários");
        List<UserResponseDTO> users = userRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        log.debug("Encontrados {} usuários", users.size());
        return users;
    }

    public UserResponseDTO createUser(UserDTO userDTO) {
        log.info("Criando um novo usuário com o email: {}", userDTO.getEmail());
        
        log.debug("Verificando se email já está cadastrado: {}", userDTO.getEmail());
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            log.warn("Tentativa de criar usuário com email já cadastrado: {}", userDTO.getEmail());
            throw new IllegalArgumentException("Email já cadastrado");
        }
        
        log.debug("Verificando se CPF já está cadastrado: {}", userDTO.getCpf());
        if (userRepository.findByCpf(userDTO.getCpf()) != null) {
            log.warn("Tentativa de criar usuário com CPF já cadastrado: {}", userDTO.getCpf());
            throw new IllegalArgumentException("CPF já cadastrado");
        }
        
        log.debug("Convertendo DTO para entidade e criptografando senha");
        UserEntity userEntity = convertToEntity(userDTO);
        userEntity.setPassword(BcryptUtils.encryptPassword(userEntity.getPassword()));
        
        UserEntity savedUser = userRepository.save(userEntity);
        log.info("Usuário criado com sucesso. ID: {}, Email: {}", savedUser.getId(), savedUser.getEmail());
        return convertToResponseDTO(savedUser);
    }

    public void authenticate(String email, String password) {
        log.debug("Tentativa de autenticação para o email: {}", email);
        UserEntity user = userRepository.findByEmail(email);
        
        // Se o usuário não existir ou a senha não coincidir, lança uma exceção
        if (user == null) {
            log.warn("Tentativa de login com email inexistente: {}", email);
            throw new IllegalArgumentException("Credenciais inválidas!");
        }
        
        if (!BcryptUtils.comparePasswords(password, user.getPassword())) {
            log.warn("Tentativa de login com senha incorreta para o email: {}", email);
            throw new IllegalArgumentException("Credenciais inválidas!");
        }
        
        log.info("Autenticação realizada com sucesso para o email: {}", email);
        // Se chegou até aqui, a autenticação foi bem-sucedida
        // Não retorna nada, apenas não lança exceção
    }

    public UserResponseDTO findUser(String email) {
        log.debug("Buscando usuário pelo email: {}", email);
        UserEntity user = userRepository.findByEmail(email);
        if (user != null) {
            log.debug("Usuário encontrado. ID: {}, Email: {}", user.getId(), user.getEmail());
            return convertToResponseDTO(user);
        } else {
            log.debug("Usuário não encontrado para o email: {}", email);
            return null;
        }
    }

    // Métodos de conversão
    private UserEntity convertToEntity(UserDTO userDTO) {
        log.trace("Convertendo UserDTO para UserEntity");
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userDTO.getName());
        userEntity.setCpf(userDTO.getCpf());
        userEntity.setPassword(userDTO.getPassword());
        userEntity.setEmail(userDTO.getEmail());
        return userEntity;
    }

    private UserResponseDTO convertToResponseDTO(UserEntity userEntity) {
        log.trace("Convertendo UserEntity para UserResponseDTO");
        return new UserResponseDTO(
            userEntity.getId(),
            userEntity.getName(),
            userEntity.getCpf(),
            userEntity.getEmail()
        );
    }
}
