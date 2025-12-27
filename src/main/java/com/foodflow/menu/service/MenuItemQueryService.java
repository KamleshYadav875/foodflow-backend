package com.foodflow.menu.service;

import com.foodflow.menu.entity.MenuItems;

import java.util.Optional;

public interface MenuItemQueryService {

    Optional<MenuItems> getMenuItemById(Long id);
}
