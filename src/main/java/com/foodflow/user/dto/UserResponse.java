package com.foodflow.user.dto;

import com.foodflow.user.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String name;
    private String phone;
    private UserRole role;
    private String profileImageUrl;
}
