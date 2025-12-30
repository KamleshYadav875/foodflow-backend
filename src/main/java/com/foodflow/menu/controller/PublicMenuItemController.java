package com.foodflow.menu.controller;

import com.foodflow.menu.dto.MenuItemResponseDto;
import com.foodflow.menu.dto.RestaurantMenuResponseDto;
import com.foodflow.menu.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public/menuitem")
public class PublicMenuItemController {

    private final MenuItemService menuItemService;

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponseDto> getMenuItemById(@PathVariable Long id){
        MenuItemResponseDto response = menuItemService.getMenuItemById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<MenuItemResponseDto>> getAllMenuItems(){
        List<MenuItemResponseDto> response = menuItemService.getAllMenuItems();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<RestaurantMenuResponseDto> getMenuItemsByRestaurant(@PathVariable Long restaurantId){
        RestaurantMenuResponseDto response = menuItemService.getMenuItemsByRestaurant(restaurantId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
