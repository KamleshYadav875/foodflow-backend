package com.foodflow.delivery.controller;

import com.foodflow.delivery.service.DeliveryAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delivery/orders")
@RequiredArgsConstructor
public class DeliveryAssignmentController {

    private final DeliveryAssignmentService assignmentService;

    @PostMapping("/{orderId}/accept")
    public ResponseEntity<Void> acceptOrder(
            @PathVariable Long orderId,
            @RequestParam Long partnerId
    ) {
        assignmentService.acceptOrder(orderId, partnerId);
        return ResponseEntity.ok().build();
    }
}
