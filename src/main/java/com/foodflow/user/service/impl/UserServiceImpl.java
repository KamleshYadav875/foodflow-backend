package com.foodflow.user.service.impl;

import com.foodflow.common.exceptions.ResourceNotFoundException;
import com.foodflow.common.util.Constant;
import com.foodflow.order.service.OrderCommandService;
import com.foodflow.order.service.OrderStatsQueryService;
import com.foodflow.user.dto.UserProfileResponseDto;
import com.foodflow.user.entity.User;
import com.foodflow.user.repository.UserRepository;
import com.foodflow.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final OrderStatsQueryService orderStatsQueryService;

    private final OrderCommandService orderCommandService;


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
                .roles(user.getRoles())
                .build();
    }

    @Override
    public void cancelOrder(Long userId, Long orderId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.USER_NOT_FOUND));

        orderCommandService.cancelOrder(user.getId(), orderId);
    }
}
