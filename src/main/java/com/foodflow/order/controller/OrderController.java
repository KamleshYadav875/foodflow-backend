package com.foodflow.order.controller;

import com.foodflow.order.dto.OrderResponseDto;
import com.foodflow.order.dto.UpdateOrderStatusRequest;
import com.foodflow.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout/{userId}")
    public ResponseEntity<OrderResponseDto> checkout(
            @PathVariable Long userId) {

        return new ResponseEntity<>(
                orderService.checkout(userId),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable Long orderId, @RequestBody UpdateOrderStatusRequest request){
        return ResponseEntity.ok(
                orderService.updateOrderStatus(
                        orderId,
                        request.getStatus()
                )
        );
    }
}
