package com.foodflow.order.controller;

import com.foodflow.order.dto.OrderDetailResponse;
import com.foodflow.order.dto.OrderResponseDto;
import com.foodflow.order.dto.PageResponse;
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

    @GetMapping("/me")
    public ResponseEntity<PageResponse<OrderResponseDto>> getUserOrders(@RequestHeader("X-USER-ID") Long userId, @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "1") int size){
        return ResponseEntity.ok(
                orderService.getOrdersByUser(userId, page, size)
        );
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrderDetails(@RequestHeader("X-USER-ID") Long userId, @PathVariable Long orderId){
        return ResponseEntity.ok(
                orderService.getUserOrderDetails(userId, orderId)
        );
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<PageResponse<OrderResponseDto>> getRestaurantOrders(
            @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ){
        return ResponseEntity.ok(orderService.getOrderByRestaurant(restaurantId, page,size));
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
