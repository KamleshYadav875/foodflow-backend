package com.foodflow.menu.service.impl;

import com.foodflow.menu.entity.MenuItems;
import com.foodflow.menu.repository.MenuItemRepository;
import com.foodflow.menu.service.MenuItemQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuItemQueryServiceImpl implements MenuItemQueryService {

    private final MenuItemRepository menuItemRepository;

    @Override
    public Optional<MenuItems> getMenuItemById(Long id) {
        return menuItemRepository.findById(id);
    }
}
