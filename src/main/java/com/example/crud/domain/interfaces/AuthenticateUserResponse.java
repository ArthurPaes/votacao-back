package com.example.crud.domain.interfaces;

import com.example.crud.domain.model.User;

public interface AuthenticateUserResponse {
    User getUser();
    String getErrorMessage();
}
