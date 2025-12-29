package com.foodflow.order.controller;

import com.foodflow.order.dto.AddToCartRequest;
import com.foodflow.order.dto.CartResponseDto;
import com.foodflow.order.service.CartService;
import com.foodflow.security.util.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
@PreAuthorize("hasRole('USER')")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartResponseDto> addItem(
            @RequestBody @Valid AddToCartRequest request) {
        return ResponseEntity.ok(
                cartService.addItem(request)
        );
    }

    @GetMapping("/user")
    public ResponseEntity<CartResponseDto> getCart() {
        Long userId = SecurityUtils.getCurrentUserId();
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

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        Long userId = SecurityUtils.getCurrentUserId();
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}