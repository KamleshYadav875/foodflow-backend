package com.foodflow.user.service;

import com.foodflow.user.dto.UserProfileResponseDto;

public interface UserService {

   UserProfileResponseDto getMyProfile(Long userId);

    void cancelOrder(Long userId, Long orderId);
}
