package com.foodflow.security.service;


import com.foodflow.security.dto.JwtResponse;
import com.foodflow.security.dto.LoginRequest;
import com.foodflow.security.dto.UserRegisterResponse;
import com.foodflow.security.dto.UserCreateRequest;

public interface AuthService {

    JwtResponse login(LoginRequest request);

    UserRegisterResponse registerUser(UserCreateRequest request);
}
