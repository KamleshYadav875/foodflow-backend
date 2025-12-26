package com.foodflow.restaurant.dto;

import lombok.Data;

@Data

public class RestaurantDetailResponseDto {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String city;
    private String state;
    private String zipcode;
    private Boolean isOpen;
    private Float rating;
    private String imageUrl;
    private Long ownerId;
}
