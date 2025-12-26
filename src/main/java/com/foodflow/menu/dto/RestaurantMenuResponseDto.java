package com.foodflow.menu.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RestaurantMenuResponseDto {
    private RestaurantSummaryDto restaurant;
    private List<MenuCategoryResponseDto> categories;
}
