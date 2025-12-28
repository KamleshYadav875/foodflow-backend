package com.foodflow.menu.service.impl;

import com.foodflow.common.exceptions.ResourceNotFoundException;
import com.foodflow.common.util.Constant;
import com.foodflow.filestorage.service.FileStorageService;
import com.foodflow.menu.dto.*;
import com.foodflow.menu.entity.MenuItems;
import com.foodflow.menu.repository.MenuItemRepository;
import com.foodflow.menu.service.MenuItemService;
import com.foodflow.restaurant.entity.Restaurant;
import com.foodflow.restaurant.service.RestaurantQueryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

    private final RestaurantQueryService restaurantQueryService;

    private final MenuItemRepository menuItemRepository;

    private final ModelMapper modelMapper;

    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    @CacheEvict(
            value = {
                  "allMenuItems" ,
                  "menuByRestaurant"
            },
            allEntries = true
    )
    public MenuItemResponseDto createMenuItem(MenuItemRequestDto request, MultipartFile image) {
        Restaurant restaurant = restaurantQueryService.getRestaurantById(request.getRestaurantId()).orElseThrow(() -> new ResourceNotFoundException(Constant.RESTAURANT_NOT_FOUND));

        String imageUrl = null;
        if(image != null && !image.isEmpty())
            imageUrl = fileStorageService.upload(image, "menuitem");

        MenuItems menuItems = MenuItems.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .isVeg(request.getIsVeg())
                .isAvailable(true)
                .category(request.getCategory())
                .imageUrl(imageUrl)
                .restaurant(restaurant)
                .build();

        MenuItems savedMenuItem = menuItemRepository.save(menuItems);
        return modelMapper.map(savedMenuItem, MenuItemResponseDto.class);
    }

    @Override
    @Cacheable(value = "menuItem", key = "#id")
    public MenuItemResponseDto getMenuItemById(Long id) {
        MenuItems menuItems = menuItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id "+id));
        return modelMapper.map(menuItems, MenuItemResponseDto.class);
    }

    @Override
    @Cacheable("allMenuItems")
    public List<MenuItemResponseDto> getAllMenuItems() {
        List<MenuItems> menuItems = menuItemRepository.findAll();
        return menuItems.stream().map(item -> modelMapper.map(item, MenuItemResponseDto.class)).toList();

    }

    @Override
    @Cacheable(value = "menuByRestaurant", key = "#restaurantId")
    public RestaurantMenuResponseDto getMenuItemsByRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantQueryService.getRestaurantById(restaurantId).orElseThrow(() -> new ResourceNotFoundException(Constant.RESTAURANT_NOT_FOUND));
        List<MenuItems> menuItems = menuItemRepository.findByRestaurant(restaurant);

        Map<String , List<MenuItemResponseDto>> grouped  =  menuItems.stream()
                .map(item -> modelMapper.map(item, MenuItemResponseDto.class))
                .collect(Collectors.groupingBy(MenuItemResponseDto::getCategory));

        List<MenuCategoryResponseDto> categories =
                grouped.entrySet().stream()
                        .map(entry -> MenuCategoryResponseDto.builder()
                                .category(entry.getKey())
                                .items(entry.getValue())
                                .build())
                        .toList();

        return RestaurantMenuResponseDto.builder()
                .categories(categories)
                .restaurant(modelMapper.map(restaurant, RestaurantSummaryDto.class))
                .build();

    }

    @Override
    public MenuItemResponseDto updateMenuItem(Long id, MenuItemRequestDto request) {
        return null;
    }

    @Override
    public void deleteMenuItem(Long id) {
        // TODO
    }
}
