package com.foodflow.user.service;

import com.foodflow.user.dto.UserCreateRequest;
import com.foodflow.user.dto.UserResponse;

public interface UserService {

    UserResponse createUser(UserCreateRequest request);

    UserResponse getUserById(Long id);
}
