package com.foodflow.user.dto;

import com.foodflow.user.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String name;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zipcode;
    private Set<UserRole> roles;
    private String profileImageUrl;

}
