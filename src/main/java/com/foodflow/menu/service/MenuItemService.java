package com.foodflow.menu.service;

import com.foodflow.menu.dto.MenuItemRequestDto;
import com.foodflow.menu.dto.MenuItemResponseDto;
import com.foodflow.menu.dto.RestaurantMenuResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MenuItemService {

    MenuItemResponseDto createMenuItem(MenuItemRequestDto request, MultipartFile image);

    MenuItemResponseDto getMenuItemById(Long id);

    List<MenuItemResponseDto> getAllMenuItems();

    RestaurantMenuResponseDto getMenuItemsByRestaurant(Long restaurantId);
}
