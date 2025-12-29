package com.foodflow.delivery.controller;

import com.foodflow.delivery.dto.*;
import com.foodflow.delivery.service.DeliveryPartnerService;
import com.foodflow.security.util.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/delivery-partners")
public class DeliveryPartnerController {

    private final DeliveryPartnerService deliveryPartnerService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/register")
    public ResponseEntity<DeliveryPartnerResponseDto> register(
            @RequestBody @Valid RegisterDeliveryPartnerRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(deliveryPartnerService.register(request));
    }

    @PreAuthorize("hasRole('DELIVERY')")
    @GetMapping("/me")
    public ResponseEntity<PartnerProfileResponseDto> getPartnerProfile() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(deliveryPartnerService.getPartnerProfile(userId));
    }

    @PreAuthorize("hasRole('DELIVERY')")
    @GetMapping("/orders/current")
    public ResponseEntity<List<PartnerOrderDetail>> getPartnerCurrentOrder() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(deliveryPartnerService.getPartnerCurrentOrder(userId));
    }

    @PreAuthorize("hasRole('DELIVERY')")
    @GetMapping("/orders/history")
    public ResponseEntity<List<PartnerOrderDetail>> getPartnerOrderHistory() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(deliveryPartnerService.getPartnerOrderHistory(userId));
    }

    @PreAuthorize("hasRole('DELIVERY')")
    @PutMapping("/{partnerId}/availability")
    public ResponseEntity<DeliveryPartnerResponseDto> updateAvailability(@PathVariable Long partnerId, @RequestBody UpdateAvailabilityRequest request){
        return ResponseEntity.ok(
                deliveryPartnerService.updateAvailability(partnerId, request.getAvailability())
        );
    }

    @PreAuthorize("hasRole('DELIVERY')")
    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<Void> updateDeliveryStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateDeliveryStatusRequest request
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        deliveryPartnerService.updateStatus(
                userId,
                orderId,
                request.getStatus()
        );
        return ResponseEntity.noContent().build();
    }
}
