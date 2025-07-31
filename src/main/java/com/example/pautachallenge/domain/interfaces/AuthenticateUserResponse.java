package com.example.pautachallenge.domain.interfaces;

import com.example.pautachallenge.domain.dto.UserResponseDTO;

public interface AuthenticateUserResponse {
    UserResponseDTO getUser();
    String getErrorMessage();
}
