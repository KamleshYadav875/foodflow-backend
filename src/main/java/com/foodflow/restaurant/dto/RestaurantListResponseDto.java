package com.foodflow.restaurant.dto;

import lombok.Data;

@Data
public class RestaurantListResponseDto {

    private Long id;
    private String name;
    private String description;
    private Boolean isOpen;
    private Float rating;
    private String imageUrl;
}
