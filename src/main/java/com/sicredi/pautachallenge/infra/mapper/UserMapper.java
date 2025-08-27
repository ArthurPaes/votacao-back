package com.sicredi.pautachallenge.infra.mapper;

import com.sicredi.pautachallenge.domain.dto.UserDTO;
import com.sicredi.pautachallenge.domain.dto.UserResponseDTO;
import com.sicredi.pautachallenge.domain.model.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserMapper {

    public UserEntity toEntity(UserDTO userDTO) {
        log.trace("Convertendo UserDTO para UserEntity: {}", userDTO.email());
        
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userDTO.name());
        userEntity.setCpf(userDTO.cpf());
        userEntity.setPassword(userDTO.password());
        userEntity.setEmail(userDTO.email());
        
        log.trace("Conversão concluída para UserEntity");
        return userEntity;
    }

    public UserResponseDTO toResponseDTO(UserEntity userEntity) {
        log.trace("Convertendo UserEntity para UserResponseDTO: {}", userEntity.getEmail());
        
        UserResponseDTO userResponseDTO = new UserResponseDTO(
            userEntity.getId(),
            userEntity.getName(),
            userEntity.getCpf(),
            userEntity.getEmail()
        );
        
        log.trace("Conversão concluída para UserResponseDTO");
        return userResponseDTO;
    }

    public UserDTO toDTO(UserEntity userEntity) {
        log.trace("Convertendo UserEntity para UserDTO: {}", userEntity.getEmail());
        
        UserDTO userDTO = new UserDTO(
            userEntity.getName(),
            userEntity.getCpf(),
            userEntity.getPassword(),
            userEntity.getEmail()
        );
        
        log.trace("Conversão concluída para UserDTO");
        return userDTO;
    }

    public UserEntity updateEntityFromDTO(UserEntity userEntity, UserDTO userDTO) {
        log.trace("Atualizando UserEntity com dados do UserDTO: {}", userDTO.email());
        
        userEntity.setName(userDTO.name());
        userEntity.setCpf(userDTO.cpf());
        userEntity.setPassword(userDTO.password());
        userEntity.setEmail(userDTO.email());
        
        log.trace("Atualização concluída");
        return userEntity;
    }
} 