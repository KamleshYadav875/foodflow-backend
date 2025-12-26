package com.foodflow.menu.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
public class MenuItemRequestDto {
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private Boolean isVeg;
    private Boolean isAvailable;
    private Long restaurantId;
}
