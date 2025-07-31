package com.example.pautachallenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pautachallenge.domain.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findByCpf(String cpf);
}