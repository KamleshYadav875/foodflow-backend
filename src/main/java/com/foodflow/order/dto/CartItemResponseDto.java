package com.foodflow.order.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemResponseDto {
    private Long id;
    private Long menuItemId;
    private String menuName;
    private String menuDescription;
    private String menuImageUrl;
    private BigDecimal totalPrice;
    private Integer quantity;
    private BigDecimal price;
}
