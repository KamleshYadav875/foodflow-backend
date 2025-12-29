package com.foodflow.security.dto;

import com.foodflow.user.enums.UserRole;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserRegisterResponse {
    private Long id;
    private String name;
    private String phone;
    private Set<UserRole> roles;
    private String profileImageUrl;
}
