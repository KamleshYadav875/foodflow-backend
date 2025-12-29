package com.foodflow.security.service.impl;

import com.foodflow.common.exceptions.BadRequestException;
import com.foodflow.security.dto.JwtResponse;
import com.foodflow.security.dto.LoginRequest;
import com.foodflow.security.dto.UserRegisterResponse;
import com.foodflow.security.jwt.JwtService;
import com.foodflow.security.service.AuthService;
import com.foodflow.security.dto.UserCreateRequest;
import com.foodflow.user.entity.User;
import com.foodflow.user.enums.UserRole;
import com.foodflow.user.enums.UserStatus;
import com.foodflow.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtResponse login(LoginRequest request) {

        //User user = userRepository.findByPhone(request.getPhone())
        //        .orElseThrow(() -> new BadRequestException("Invalid phone number"));

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getPhone(), request.getPassword()));

        User user = (User) authentication.getPrincipal();

        String token = jwtService.generateToken(user);

        return JwtResponse.builder()
                .token(token)
                .roles(user.getRoles())
                .build();
    }

    @Override
    public UserRegisterResponse registerUser(UserCreateRequest request) {
        if (request.getPhone() == null || request.getPhone().trim().isEmpty()) {
            throw new BadRequestException("Phone number should not be blank");
        }

        if (!request.getPhone().matches("^[6-9]\\d{9}$")) {
            throw new BadRequestException("Phone number must be a valid 10-digit Indian number");
        }

        if(userRepository.existsByPhone(request.getPhone())){
            throw new BadRequestException("User already registered with phone number");
        }

        User user = User.builder()
                .phone(request.getPhone())
                .name(request.getName())
                .roles(Set.of(UserRole.USER))
                .password(passwordEncoder.encode(request.getPassword()))
                .status(UserStatus.ACTIVE)
                .build();

        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserRegisterResponse.class);
    }


}
