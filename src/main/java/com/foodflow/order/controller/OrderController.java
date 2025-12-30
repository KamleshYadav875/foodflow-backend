package com.foodflow.order.controller;

import com.foodflow.order.dto.*;
import com.foodflow.order.service.OrderService;
import com.foodflow.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/checkout")
    public ResponseEntity<OrderCheckoutResponseDto> checkout() {
        Long userId = SecurityUtils.getCurrentUserId();
        return new ResponseEntity<>(
                orderService.checkout(userId),
                HttpStatus.CREATED
        );
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me")
    public ResponseEntity<PageResponse<UserOrderResponseDto>> getUserOrders(@RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "1") int size){
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(
                orderService.getOrdersByUser(userId, page, size)
        );
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrderDetails(@PathVariable Long orderId){
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(
                orderService.getUserOrderDetails(userId, orderId)
        );
    }

    @PreAuthorize("hasRole('RESTAURANT')")
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<PageResponse<OrderResponseDto>> getRestaurantOrders(
            @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ){
        return ResponseEntity.ok(orderService.getOrderByRestaurant(restaurantId, page,size));
    }

    @PreAuthorize("hasRole('RESTAURANT')")
    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderUpdateResponseDto> updateOrderStatus(@PathVariable Long orderId, @RequestBody UpdateOrderStatusRequest request){
        return ResponseEntity.ok(
                orderService.updateOrderStatus(
                        orderId,
                        request
                )
        );
    }

}
