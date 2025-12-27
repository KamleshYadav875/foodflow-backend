package com.foodflow.order.controller;

import com.foodflow.order.dto.AddToCartRequest;
import com.foodflow.order.dto.CartResponseDto;
import com.foodflow.order.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartResponseDto> addItem(
            @RequestBody @Valid AddToCartRequest request) {
        return ResponseEntity.ok(
                cartService.addItem(request)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CartResponseDto> getCart(
            @PathVariable Long userId) {
        return ResponseEntity.ok(
                cartService.getCartByUser(userId)
        );
    }

    @PatchMapping("/item/{cartItemId}")
    public ResponseEntity<CartResponseDto> updateItem(
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(
                cartService.updateItem(cartItemId, quantity)
        );
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<Void> removeItem(
            @PathVariable Long cartItemId) {
        cartService.removeItem(cartItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<Void> clearCart(
            @PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}