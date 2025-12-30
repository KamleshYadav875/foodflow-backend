package com.foodflow.user.service;

import com.foodflow.user.dto.UpdateUserProfileRequest;
import com.foodflow.user.dto.UserProfileResponseDto;
import com.foodflow.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {

   UserProfileResponseDto getMyProfile(Long userId);

    void cancelOrder(Long userId, Long orderId);

     Optional<User> getUserByEmail(String email );

    User saveUser(User newUser);

    void updateUserImage(MultipartFile image);

    UserProfileResponseDto updateProfile(UpdateUserProfileRequest request);
}
