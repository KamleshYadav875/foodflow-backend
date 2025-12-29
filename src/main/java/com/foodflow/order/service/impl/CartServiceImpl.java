package com.foodflow.order.service.impl;

import com.foodflow.common.exceptions.ResourceNotFoundException;
import com.foodflow.common.util.Constant;
import com.foodflow.menu.entity.MenuItems;
import com.foodflow.menu.service.MenuItemQueryService;
import com.foodflow.order.dto.AddToCartRequest;
import com.foodflow.order.dto.CartItemResponseDto;
import com.foodflow.order.dto.CartResponseDto;
import com.foodflow.order.entity.Cart;
import com.foodflow.order.entity.CartItem;
import com.foodflow.order.repository.CartItemRepository;
import com.foodflow.order.repository.CartRepository;
import com.foodflow.order.service.CartService;
import com.foodflow.restaurant.entity.Restaurant;
import com.foodflow.restaurant.service.RestaurantQueryService;
import com.foodflow.security.util.SecurityUtils;
import com.foodflow.user.entity.User;
import com.foodflow.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import com.foodflow.common.exceptions.BadRequestException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final ModelMapper modelMapper;
    private final CartRepository cartRepository;
    private final UserQueryService userQueryService;
    private final RestaurantQueryService restaurantQueryService;
    private final MenuItemQueryService menuItemQueryService;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public CartResponseDto addItem(AddToCartRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        User user = userQueryService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.USER_NOT_FOUND));

        Restaurant restaurant = restaurantQueryService
                .getRestaurantById(request.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException(Constant.RESTAURANT_NOT_FOUND));

        MenuItems menuItem = menuItemQueryService
                .getMenuItemById(request.getMenuItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        if (request.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than zero");
        }

        Cart cart = cartRepository.findByUser(user);

        if(ObjectUtils.isEmpty(cart)) {
            cart = Cart.builder()
                    .restaurant(restaurant)
                    .user(user)
                    .totalItems(0)
                    .totalAmount(BigDecimal.ZERO)
                    .build();
            cart = cartRepository.save(cart);
        }

        if (!cart.getRestaurant().getId().equals(restaurant.getId())) {
            throw new BadRequestException(
                    "Cart already contains items from another restaurant"
            );
        }

        if(Boolean.FALSE.equals(menuItem.getIsAvailable()))
            throw new BadRequestException("Menu item is not available");

        CartItem cartItem = cartItemRepository
                .findByCartAndMenuItems(cart, menuItem)
                .orElse(null);

        BigDecimal itemTotal =
                menuItem.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));

        if (cartItem == null) {
            cartItem = CartItem.builder()
                    .cart(cart)
                    .menuItems(menuItem)
                    .price(menuItem.getPrice())
                    .quantity(request.getQuantity())
                    .totalPrice(itemTotal)
                    .build();
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
            cartItem.setTotalPrice(
                    cartItem.getPrice().multiply(
                            BigDecimal.valueOf(cartItem.getQuantity()))
            );
        }

        cartItemRepository.save(cartItem);

        cart.setTotalItems(cart.getTotalItems() + request.getQuantity());
        cart.setTotalAmount(cart.getTotalAmount().add(itemTotal));
        cartRepository.save(cart);

        return buildCartResponse(cart);

    }

    private CartResponseDto buildCartResponse(Cart cart) {
        List<CartItem> items = cartItemRepository.findByCart(cart);

        return CartResponseDto.builder()
                .cartId(cart.getId())
                .restaurantId(cart.getRestaurant().getId())
                .totalAmount(cart.getTotalAmount())
                .totalItems(cart.getTotalItems())
                .items(
                        items.stream()
                                .map(item -> modelMapper.map(item, CartItemResponseDto.class))
                                .toList()
                )
                .build();
    }

    @Override
    public CartResponseDto getCartByUser(Long userId) {
        User user  = userQueryService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.USER_NOT_FOUND));
        Cart cart = cartRepository.findByUser(user);

        if (cart == null) {
            throw new ResourceNotFoundException("Cart is empty");
        }

        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponseDto updateItem(Long cartItemId, Integer quantity) {

        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than zero");
        }

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found in cart"));

        Cart cart = cartItem.getCart();

        int oldQuantity = cartItem.getQuantity();
        BigDecimal oldTotal = cartItem.getTotalPrice();

        cartItem.setQuantity(quantity);

        BigDecimal itemTotal = cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        cartItem.setTotalPrice(itemTotal);

        cartItemRepository.save(cartItem);

        cart.setTotalItems(cart.getTotalItems() - oldQuantity + quantity);
        cart.setTotalAmount(cart.getTotalAmount().subtract(oldTotal).add(itemTotal));
        cartRepository.save(cart);

        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public void removeItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found in cart"));

        Cart cart = cartItem.getCart();

        cart.setTotalItems(cart.getTotalItems() - cartItem.getQuantity());
        cart.setTotalAmount(cart.getTotalAmount().subtract(cartItem.getTotalPrice()));

        cartItemRepository.delete(cartItem);

        if (cart.getTotalItems() <= 0) {
            cartRepository.delete(cart);
        } else {
            cartRepository.save(cart);
        }

    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        User user = userQueryService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.USER_NOT_FOUND));
        Cart cart = cartRepository.findByUser(user);
        cartItemRepository.deleteByCart(cart);
        cartRepository.delete(cart);
    }
}
