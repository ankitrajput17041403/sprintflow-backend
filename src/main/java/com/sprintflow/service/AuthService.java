package com.sprintflow.service;

import com.sprintflow.dto.AuthResponse;
import com.sprintflow.dto.LoginRequest;
import com.sprintflow.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}