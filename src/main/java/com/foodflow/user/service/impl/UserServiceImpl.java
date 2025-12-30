package com.foodflow.user.service.impl;

import com.foodflow.common.exceptions.BadRequestException;
import com.foodflow.common.exceptions.ResourceNotFoundException;
import com.foodflow.common.util.Constant;
import com.foodflow.filestorage.service.FileStorageService;
import com.foodflow.order.service.OrderCommandService;
import com.foodflow.order.service.OrderStatsQueryService;
import com.foodflow.security.util.SecurityUtils;
import com.foodflow.user.dto.UpdateUserProfileRequest;
import com.foodflow.user.dto.UserProfileResponseDto;
import com.foodflow.user.entity.User;
import com.foodflow.user.repository.UserRepository;
import com.foodflow.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final OrderStatsQueryService orderStatsQueryService;

    private final OrderCommandService orderCommandService;

    private final FileStorageService fileStorageService;


    @Override
    public UserProfileResponseDto getMyProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not fount "));

        long totalOrders = orderStatsQueryService.countTotalOrders(userId);
        long activeOrders = orderStatsQueryService.countActiveOrders(userId);
        long cancelledOrders = orderStatsQueryService.countCancelledOrders(userId);

        return UserProfileResponseDto.builder()
                .name(user.getName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .address(user.getAddress())
                .city(user.getCity())
                .state(user.getState())
                .zipcode(user.getZipcode())
                .userId(user.getId())
                .totalOrders((int) totalOrders)
                .cancelledOrders((int) cancelledOrders)
                .activeOrders((int) activeOrders)
                .joinedAt(user.getCreatedAt())
                .roles(user.getRoles())
                .build();
    }

    @Override
    public void cancelOrder(Long userId, Long orderId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.USER_NOT_FOUND));

        orderCommandService.cancelOrder(user.getId(), orderId);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User saveUser(User newUser) {
        return userRepository.save(newUser);
    }

    @Override
    public void updateUserImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new BadRequestException("Image file is required");
        }
        Long userId = SecurityUtils.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.USER_NOT_FOUND));

        String newImageUrl = fileStorageService.upload(image, "restaurant");
        user.setProfileImageUrl(newImageUrl);

        userRepository.save(user);

    }

    @Override
    public UserProfileResponseDto updateProfile(UpdateUserProfileRequest request) {

        Long userId = SecurityUtils.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.USER_NOT_FOUND));

        long totalOrders = orderStatsQueryService.countTotalOrders(userId);
        long activeOrders = orderStatsQueryService.countActiveOrders(userId);
        long cancelledOrders = orderStatsQueryService.countCancelledOrders(userId);

        user.setName(request.getName());
        user.setAddress(request.getAddress());
        user.setCity(request.getCity());
        user.setState(request.getState());
        user.setZipcode(request.getZipcode());

        User updatedUser = userRepository.save(user);
        return UserProfileResponseDto.builder()
                .userId(updatedUser.getId())
                .name(updatedUser.getName())
                .phone(updatedUser.getPhone())
                .email(updatedUser.getEmail())
                .profileImageUrl(updatedUser.getProfileImageUrl())
                .address(updatedUser.getAddress())
                .city(updatedUser.getCity())
                .state(updatedUser.getState())
                .zipcode(updatedUser.getZipcode())
                .totalOrders((int) totalOrders)
                .activeOrders((int) activeOrders)
                .cancelledOrders((int) cancelledOrders)
                .roles(updatedUser.getRoles())
                .joinedAt(updatedUser.getCreatedAt())
                .build();
    }
}
