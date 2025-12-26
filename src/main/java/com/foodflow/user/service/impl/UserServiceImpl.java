package com.foodflow.user.service.impl;

import com.foodflow.common.exceptions.BadRequestException;
import com.foodflow.user.dto.UserCreateRequest;
import com.foodflow.user.dto.UserResponse;
import com.foodflow.user.entity.User;
import com.foodflow.user.enums.UserStatus;
import com.foodflow.user.repository.UserRepository;
import com.foodflow.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;

    private final UserRepository userRepository;

    @Override
    public UserResponse createUser(UserCreateRequest request) {

        if (request.getPhone() == null || request.getPhone().trim().isEmpty()) {
            throw new BadRequestException("Phone number should not be blank");
        }

        if (!request.getPhone().matches("^[6-9]\\d{9}$")) {
            throw new BadRequestException("Phone number must be a valid 10-digit Indian number");
        }

        User user = User.builder()
                .phone(request.getPhone())
                .name(request.getName())
                .role(request.getRole())
                .status(UserStatus.ACTIVE)
                .build();

        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserResponse.class);

    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not fount with id "+id));
        return  modelMapper.map(user, UserResponse.class);
    }
}
