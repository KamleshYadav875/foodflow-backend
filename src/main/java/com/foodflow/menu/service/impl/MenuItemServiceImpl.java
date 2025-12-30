package com.foodflow.menu.service.impl;

import com.foodflow.common.exceptions.BadRequestException;
import com.foodflow.common.exceptions.ResourceNotFoundException;
import com.foodflow.common.util.Constant;
import com.foodflow.filestorage.service.FileStorageService;
import com.foodflow.menu.dto.*;
import com.foodflow.menu.entity.MenuItems;
import com.foodflow.menu.repository.MenuItemRepository;
import com.foodflow.menu.service.MenuItemService;
import com.foodflow.restaurant.entity.Restaurant;
import com.foodflow.restaurant.service.RestaurantQueryService;
import com.foodflow.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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
    @Caching( evict = {
            @CacheEvict(value = "allMenuItems", allEntries = true),
            @CacheEvict(value = "menuByRestaurant", allEntries = true),
            @CacheEvict(value = "menuItem", key = "#menuItemId")
        }
    )
    public void updateMenuItemAvailability(Long menuItemId) {
        Long userId = SecurityUtils.getCurrentUserId();

        MenuItems menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found"));

        if(!menuItem.getRestaurant().getOwner().getId().equals(userId)){
            throw new BadRequestException("You are not allowed to perform the action");
        }

        menuItem.setIsAvailable(!menuItem.getIsAvailable());
        menuItemRepository.save(menuItem);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "menuItem", key = "#menuItemId"),
                    @CacheEvict(value = "menuByRestaurant", allEntries = true),
                    @CacheEvict(value = "allMenuItems", allEntries = true),
            }
    )
    public void updateMenuItemImage(Long menuItemId, MultipartFile image) {

        if (image == null || image.isEmpty()) {
            throw new BadRequestException("Image file is required");
        }

        MenuItems menuItems = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menuitem not found"));

        Long userId = SecurityUtils.getCurrentUserId();
        if(!menuItems.getRestaurant().getOwner().getId().equals(userId))
            throw new AuthorizationDeniedException("Not owner of the restaurant");

        String newImageUrl = fileStorageService.upload(image, "menuitem");
        menuItems.setImageUrl(newImageUrl);

        menuItemRepository.save(menuItems);
    }

    @Override
    @Caching(
            put = {
                    @CachePut(value = "menuItem", key = "#request.id")
            },
            evict = {
                    @CacheEvict(value = "menuByRestaurant", allEntries = true),
                    @CacheEvict(value = "allMenuItems", allEntries = true)
            }
    )
    public MenuItemResponseDto updateMenuItem(UpdateMenuItemRequest request) {
        MenuItems menuItems = menuItemRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found"));

        menuItems.setName(request.getName());
        menuItems.setDescription(request.getDescription());
        menuItems.setCategory(request.getCategory());
        menuItems.setPrice(request.getPrice());
        menuItems.setIsAvailable(request.getIsAvailable());
        menuItems.setIsVeg(request.getIsVeg());

        MenuItems updatedMenuItem = menuItemRepository.save(menuItems);
        return  modelMapper.map(updatedMenuItem, MenuItemResponseDto.class);
    }

}
