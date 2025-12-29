package com.foodflow.user.controller;

import com.foodflow.security.util.SecurityUtils;
import com.foodflow.user.dto.UserProfileResponseDto;
import com.foodflow.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponseDto> getMyProfile() {
        Long userId = SecurityUtils.getCurrentUserId();
        UserProfileResponseDto userProfileResponseDto = userService.getMyProfile(userId);
        return ResponseEntity.ok(userProfileResponseDto);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/orders/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.cancelOrder(userId, orderId);
        return ResponseEntity.noContent().build();
    }
}
