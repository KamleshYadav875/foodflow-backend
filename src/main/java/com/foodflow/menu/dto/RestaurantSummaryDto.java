package com.foodflow.menu.dto;

import lombok.Data;

@Data
public class RestaurantSummaryDto {
    private Long id;
    private String name;
    private String city;
    private Float rating;
}
