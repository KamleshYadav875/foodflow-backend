package com.foodflow.restaurant.service.impl;

import com.foodflow.common.exceptions.BadRequestException;
import com.foodflow.common.exceptions.ResourceNotFoundException;
import com.foodflow.common.util.Constant;
import com.foodflow.filestorage.service.FileStorageService;
import com.foodflow.menu.entity.MenuItems;
import com.foodflow.restaurant.dto.RestaurantDetailResponseDto;
import com.foodflow.restaurant.dto.RestaurantListResponseDto;
import com.foodflow.restaurant.dto.RestaurantRequestDto;
import com.foodflow.restaurant.entity.Restaurant;
import com.foodflow.restaurant.repository.RestaurantRepository;
import com.foodflow.restaurant.service.RestaurantService;
import com.foodflow.security.util.SecurityUtils;
import com.foodflow.user.entity.User;
import com.foodflow.user.service.impl.UserCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private final UserCommandService userCommandService;

    private final ModelMapper modelMapper;

    private final FileStorageService fileStorageService;

    @Override
    @CacheEvict(
            value = {
                    "allRestaurants",
                    "restaurantByCity"
            },
            allEntries = true
    )
    @Transactional
    public RestaurantDetailResponseDto createRestaurant(RestaurantRequestDto request, MultipartFile image) {

        User owner = userCommandService.onboardRestaurantAdmin(request.getPhone(), request.getName());

        if(restaurantRepository.existsByOwner(owner)){
            throw new BadRequestException("Restaurant is already registered with same phone");
        }
        String url = image == null ? null : fileStorageService.upload(image, "restaurant");
        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .description(request.getDescription())
                .owner(owner)
                .rating(0f)
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .zipcode(request.getZipcode())
                .isOpen(true)
                .imageUrl(url)
                .build();

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return modelMapper.map(savedRestaurant, RestaurantDetailResponseDto.class);
    }

    @Override
//    @Cacheable(value = "allRestaurants", sync = true)
    public List<RestaurantListResponseDto> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAllWithOwner();

        return restaurants.stream().map(
                r -> {
                    RestaurantListResponseDto dto = new RestaurantListResponseDto();
                    dto.setId(r.getId());
                    dto.setName(r.getName());
                    dto.setDescription(r.getDescription());
                    dto.setIsOpen(r.getIsOpen());
                    dto.setRating(r.getRating());
                    dto.setImageUrl(r.getImageUrl());

                    if (r.getOwner() != null)
                        dto.setOwnerName(r.getOwner().getName());
                    return dto;
                }
        ).toList();
    }

    @Override
    @Cacheable(value = "restaurantByCity", key = "#city", sync = true)
    public List<RestaurantListResponseDto> getAllRestaurantsByCity(String city) {
        List<Restaurant> restaurants = restaurantRepository.findByCity(city);
        return restaurants.stream().filter(Restaurant::getIsOpen).map(r -> modelMapper.map(r, RestaurantListResponseDto.class)).toList();
    }

    @Override
    @Cacheable(value = "restaurant", key = "#id", unless = "#result == null")
    public RestaurantDetailResponseDto getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.RESTAURANT_NOT_FOUND));

        User owner = restaurant.getOwner();
        RestaurantDetailResponseDto restaurantDetailResponse = modelMapper.map(restaurant, RestaurantDetailResponseDto.class);

        if (!ObjectUtils.isEmpty(owner))
            restaurantDetailResponse.setOwnerId(owner.getId());

        return restaurantDetailResponse;
    }

    @Override
    public void changeRestaurantState(Long restaurantId) {
        Long userId = SecurityUtils.getCurrentUserId();
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.RESTAURANT_NOT_FOUND));

        if(!restaurant.getOwner().getId().equals(userId)){
            throw new BadRequestException("You are not authorized to perform the action");
        }
        restaurant.setIsOpen(!restaurant.getIsOpen());
        restaurantRepository.save(restaurant);

    }

    @Override
    public void updateResturantImage(Long restaurantId, MultipartFile image) {

        if (image == null || image.isEmpty()) {
            throw new BadRequestException("Image file is required");
        }

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.RESTAURANT_NOT_FOUND));

        Long userId = SecurityUtils.getCurrentUserId();
        if(!restaurant.getOwner().getId().equals(userId))
            throw new AuthorizationDeniedException("Not owner of the restaurant");

        String newImageUrl = fileStorageService.upload(image, "restaurant");
        restaurant.setImageUrl(newImageUrl);

        restaurantRepository.save(restaurant);
    }

}
