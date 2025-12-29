package com.foodflow.security.dto;

import com.foodflow.user.enums.UserRole;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class JwtResponse {
    private String token;
    private Set<UserRole> roles;
}
