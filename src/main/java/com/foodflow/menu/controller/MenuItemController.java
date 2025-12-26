package com.foodflow.menu.controller;

import com.foodflow.menu.dto.MenuItemRequestDto;
import com.foodflow.menu.dto.MenuItemResponseDto;
import com.foodflow.menu.dto.RestaurantMenuResponseDto;
import com.foodflow.menu.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menuitem")
public class MenuItemController {

    private final MenuItemService menuItemService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MenuItemResponseDto> createMenuItem(@RequestPart("menuitem") MenuItemRequestDto request, @RequestPart(value = "image", required = false)MultipartFile image){
        MenuItemResponseDto response = menuItemService.createMenuItem(request, image);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

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
