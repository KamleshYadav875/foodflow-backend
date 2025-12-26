package com.foodflow.menu.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MenuItemResponseDto {
    private Long id;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private Boolean isVeg;
    private Boolean isAvailable;
    private String imageUrl;
}
