package com.foodflow.user.service.impl;

import com.foodflow.common.exceptions.BadRequestException;
import com.foodflow.common.exceptions.ResourceNotFoundException;
import com.foodflow.common.util.Constant;
import com.foodflow.order.service.OrderCommandService;
import com.foodflow.order.service.OrderStatsQueryService;
import com.foodflow.user.dto.UserCreateRequest;
import com.foodflow.user.dto.UserProfileResponseDto;
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

    private final OrderStatsQueryService orderStatsQueryService;

    private final OrderCommandService orderCommandService;

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
    public UserProfileResponseDto getMyProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not fount "));

        long totalOrders = orderStatsQueryService.countTotalOrders(userId);
        long activeOrders = orderStatsQueryService.countActiveOrders(userId);
        long cancelledOrders = orderStatsQueryService.countCancelledOrders(userId);

        return UserProfileResponseDto.builder()
                .name(user.getName())
                .phone(user.getPhone())
                .userId(user.getId())
                .totalOrders((int) totalOrders)
                .cancelledOrders((int) cancelledOrders)
                .activeOrders((int) activeOrders)
                .joinedAt(user.getCreatedAt())
                .build();
    }

    @Override
    public void cancelOrder(Long userId, Long orderId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.USER_NOT_FOUND));

        orderCommandService.cancelOrder(user.getId(), orderId);
    }
}
