package com.foodflow.user.service;

import com.foodflow.user.dto.UserResponse;
import com.foodflow.user.entity.User;

import java.util.Optional;

public interface UserQueryService {

    Optional<User> getUserById(Long id);
}
