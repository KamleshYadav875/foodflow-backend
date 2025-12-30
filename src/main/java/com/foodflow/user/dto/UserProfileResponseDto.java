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
    private String email;
    private String profileImageUrl;

    private String address;
    private String city;
    private String state;
    private String zipcode;

    private Integer totalOrders;
    private Integer activeOrders;
    private Integer cancelledOrders;
    private Set<UserRole> roles;

    private LocalDateTime joinedAt;
}
