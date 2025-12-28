package com.foodflow.user.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserProfileResponseDto {

    private Long userId;
    private String name;
    private String phone;

    private Integer totalOrders;
    private Integer activeOrders;
    private Integer cancelledOrders;

    private LocalDateTime joinedAt;
}
