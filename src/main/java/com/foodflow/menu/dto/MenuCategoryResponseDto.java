package com.foodflow.menu.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MenuCategoryResponseDto {
    private String category;
    private List<MenuItemResponseDto> items;
}
