package com.foodflow.user.dto;

import com.foodflow.user.enums.UserRole;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class UserProfileResponseDto {

    private Long userId;
    private String name;
    private String phone;

    private Integer totalOrders;
    private Integer activeOrders;
    private Integer cancelledOrders;
    private Set<UserRole> roles;

    private LocalDateTime joinedAt;
}
