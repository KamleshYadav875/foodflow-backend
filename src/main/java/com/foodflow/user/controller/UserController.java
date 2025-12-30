package com.foodflow.user.controller;

import com.foodflow.security.util.SecurityUtils;
import com.foodflow.user.dto.UpdateUserProfileRequest;
import com.foodflow.user.dto.UserProfileResponseDto;
import com.foodflow.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@PreAuthorize("hasRole('USER')")
public class UserController {

    private final UserService userService;


    @GetMapping("/me")
    public ResponseEntity<UserProfileResponseDto> getMyProfile() {
        Long userId = SecurityUtils.getCurrentUserId();
        UserProfileResponseDto userProfileResponseDto = userService.getMyProfile(userId);
        return ResponseEntity.ok(userProfileResponseDto);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileResponseDto> updateProfile(@RequestBody UpdateUserProfileRequest request){
        UserProfileResponseDto response = userService.updateProfile(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/orders/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.cancelOrder(userId, orderId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateMenuItemImage(@RequestPart(value = "image", required = false) MultipartFile image){
        userService.updateUserImage(image);
        return ResponseEntity.noContent().build();
    }
}
