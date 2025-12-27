package com.foodflow.order.service;

import com.foodflow.order.dto.AddToCartRequest;
import com.foodflow.order.dto.CartResponseDto;

public interface CartService {
    CartResponseDto addItem(AddToCartRequest request);

    CartResponseDto getCartByUser(Long userId);

    CartResponseDto updateItem(Long cartItemId, Integer quantity);

    void removeItem(Long cartItemId);

    void clearCart(Long userId);
}
