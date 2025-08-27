package com.sicredi.pautachallenge.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.sicredi.pautachallenge.domain.dto.UserDTO;
import com.sicredi.pautachallenge.domain.dto.UserResponseDTO;
import com.sicredi.pautachallenge.domain.interfaces.UserLoginRequest;
import com.sicredi.pautachallenge.domain.model.UserEntity;
import com.sicredi.pautachallenge.infra.mapper.UserMapper;
import com.sicredi.pautachallenge.repository.UserRepository;
import com.sicredi.pautachallenge.utils.BcryptUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserResponseDTO> getUsers() {
        log.debug("Buscando todos os usuários");
        List<UserResponseDTO> users = userRepository.findAll().stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
        log.debug("Encontrados {} usuários", users.size());
        return users;
    }

    public UserResponseDTO createUser(UserDTO userDTO) {
        log.info("Criando um novo usuário com o email: {}", userDTO.email());
        
        validateUserUniqueness(userDTO);
        
        UserEntity userEntity = createUserEntity(userDTO);
        UserEntity savedUser = userRepository.save(userEntity);
        
        log.info("Usuário criado com sucesso. ID: {}, Email: {}", savedUser.getId(), savedUser.getEmail());
        return userMapper.toResponseDTO(savedUser);
    }

    private void validateUserUniqueness(UserDTO userDTO) {
        validateEmailUniqueness(userDTO.email());
        validateCpfUniqueness(userDTO.cpf());
    }

    private void validateEmailUniqueness(String email) {
        log.debug("Verificando se email já está cadastrado: {}", email);
        if (userRepository.findByEmail(email) != null) {
            log.warn("Tentativa de criar usuário com email já cadastrado: {}", email);
            throw new IllegalArgumentException("Email já cadastrado");
        }
    }

    private void validateCpfUniqueness(String cpf) {
        log.debug("Verificando se CPF já está cadastrado: {}", cpf);
        if (userRepository.findByCpf(cpf) != null) {
            log.warn("Tentativa de criar usuário com CPF já cadastrado: {}", cpf);
            throw new IllegalArgumentException("CPF já cadastrado");
        }
    }

    private UserEntity createUserEntity(UserDTO userDTO) {
        log.debug("Convertendo DTO para entidade e criptografando senha");
        UserEntity userEntity = userMapper.toEntity(userDTO);
        userEntity.setPassword(BcryptUtils.encryptPassword(userEntity.getPassword()));
        return userEntity;
    }

    public void authenticate(String email, String password) {
        log.debug("Tentativa de autenticação para o email: {}", email);
        UserEntity user = findUserByEmail(email);
        validatePassword(user, password);
        log.info("Autenticação realizada com sucesso para o email: {}", email);
    }

    private UserEntity findUserByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            log.warn("Tentativa de login com email inexistente: {}", email);
            throw new IllegalArgumentException("Credenciais inválidas!");
        }
        return user;
    }

    private void validatePassword(UserEntity user, String password) {
        if (!BcryptUtils.comparePasswords(password, user.getPassword())) {
            log.warn("Tentativa de login com senha incorreta para o email: {}", user.getEmail());
            throw new IllegalArgumentException("Credenciais inválidas!");
        }
    }

    public UserResponseDTO findUser(String email) {
        log.debug("Buscando usuário pelo email: {}", email);
        UserEntity user = userRepository.findByEmail(email);
        if (user != null) {
            log.debug("Usuário encontrado. ID: {}, Email: {}", user.getId(), user.getEmail());
            return userMapper.toResponseDTO(user);
        }
        log.debug("Usuário não encontrado para o email: {}", email);
        return null;
    }

    public UserResponseDTO login(UserLoginRequest userBody) {
        log.info("Tentativa de login para o email: {}", userBody.email());
        
        authenticate(userBody.email(), userBody.password());
        UserResponseDTO user = findUser(userBody.email());
        
        log.info("Login realizado com sucesso para o email: {}", userBody.email());
        log.debug("Retornando dados do usuário: {}", user.email());
        
        return user;
    }
}
