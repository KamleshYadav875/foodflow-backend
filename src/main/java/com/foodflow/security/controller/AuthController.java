package com.foodflow.security.controller;

import com.foodflow.security.dto.JwtResponse;
import com.foodflow.security.dto.LoginRequest;
import com.foodflow.security.dto.UserRegisterResponse;
import com.foodflow.security.service.AuthService;
import com.foodflow.security.dto.UserCreateRequest;
import com.foodflow.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> registerUser(@RequestBody UserCreateRequest request) {

        UserRegisterResponse userResponse = authService.registerUser(request);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

}
