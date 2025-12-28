package com.foodflow.user.service;

import com.foodflow.user.dto.UserCreateRequest;
import com.foodflow.user.dto.UserProfileResponseDto;
import com.foodflow.user.dto.UserResponse;

public interface UserService {

    UserResponse createUser(UserCreateRequest request);



    UserProfileResponseDto getMyProfile(Long userId);

    void cancelOrder(Long userId, Long orderId);
}
