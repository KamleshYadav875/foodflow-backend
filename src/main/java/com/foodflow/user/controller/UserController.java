package com.foodflow.user.controller;

import com.foodflow.user.dto.UserCreateRequest;
import com.foodflow.user.dto.UserProfileResponseDto;
import com.foodflow.user.dto.UserResponse;
import com.foodflow.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserCreateRequest request) {

        UserResponse userResponse = userService.createUser(request);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponseDto> getMyProfile(@RequestHeader("X-USER-ID") Long userId) {
        UserProfileResponseDto userProfileResponseDto = userService.getMyProfile(userId);
        return ResponseEntity.ok(userProfileResponseDto);
    }

    @PutMapping("/orders/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@RequestHeader("X-USER-ID") Long userId, @PathVariable Long orderId) {
        userService.cancelOrder(userId, orderId);
        return ResponseEntity.noContent().build();
    }
}
