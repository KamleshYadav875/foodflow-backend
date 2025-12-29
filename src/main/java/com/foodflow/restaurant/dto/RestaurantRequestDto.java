package com.foodflow.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantRequestDto {
    private String name;
    private String description;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zipcode;
}
