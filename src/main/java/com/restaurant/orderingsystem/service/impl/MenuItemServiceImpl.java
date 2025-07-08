package com.restaurant.orderingsystem.service.impl;

import com.restaurant.orderingsystem.entity.Category;
import com.restaurant.orderingsystem.entity.MenuItem;
import com.restaurant.orderingsystem.repository.CategoryRepository;
import com.restaurant.orderingsystem.repository.MenuItemRepository;
import com.restaurant.orderingsystem.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 菜单项服务实现类
 */
@Service
public class MenuItemServiceImpl implements MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * 创建新菜单项
     *
     * @param menuItem 菜单项对象
     * @return 保存后的菜单项
     */
    @Override
    @Transactional
    public MenuItem createMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    /**
     * 根据ID查询菜单项
     *
     * @param id 菜单项ID
     * @return 菜单项对象
     */
    @Override
    public Optional<MenuItem> getMenuItemById(Long id) {
        return menuItemRepository.findById(id);
    }

    /**
     * 获取所有菜单项
     *
     * @return 菜单项列表
     */
    @Override
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    /**
     * 根据分类查询菜单项
     *
     * @param category 分类对象
     * @return 该分类下的菜单项列表
     */
    @Override
    public List<MenuItem> getMenuItemsByCategory(Category category) {
        return menuItemRepository.findByCategory(category);
    }

    /**
     * 根据分类ID查询菜单项
     *
     * @param categoryId 分类ID
     * @return 该分类下的菜单项列表
     */
    @Override
    public List<MenuItem> getMenuItemsByCategoryId(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(this::getMenuItemsByCategory)
                .orElse(List.of());
    }

    /**
     * 根据菜品名称查询菜单项
     *
     * @param name 菜品名称
     * @return 符合条件的菜单项列表
     */
    @Override
    public List<MenuItem> getMenuItemsByName(String name) {
        return menuItemRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * 根据可用性查询菜单项
     *
     * @param availability 可用性状态
     * @return 符合条件的菜单项列表
     */
    @Override
    public List<MenuItem> getMenuItemsByAvailability(MenuItem.Availability availability) {
        return menuItemRepository.findByAvailability(availability);
    }

    /**
     * 更新菜单项
     *
     * @param id              菜单项ID
     * @param menuItemDetails 更新的菜单项详情
     * @return 更新后的菜单项
     */
    @Override
    @Transactional
    public MenuItem updateMenuItem(Long id, MenuItem menuItemDetails) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("菜单项不存在，ID: " + id));

        menuItem.setName(menuItemDetails.getName());
        menuItem.setDescription(menuItemDetails.getDescription());
        menuItem.setPrice(menuItemDetails.getPrice());
        menuItem.setCategory(menuItemDetails.getCategory());
        menuItem.setImageUrl(menuItemDetails.getImageUrl());
        menuItem.setAvailability(menuItemDetails.getAvailability());

        return menuItemRepository.save(menuItem);
    }

    /**
     * 删除菜单项
     *
     * @param id 菜单项ID
     */
    @Override
    @Transactional
    public void deleteMenuItem(Long id) {
        menuItemRepository.deleteById(id);
    }

    /**
     * 更新菜单项可用性
     *
     * @param id           菜单项ID
     * @param availability 可用性状态
     * @return 更新后的菜单项
     */
    @Override
    @Transactional
    public MenuItem updateMenuItemAvailability(Long id, MenuItem.Availability availability) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("菜单项不存在，ID: " + id));

        menuItem.setAvailability(availability);
        return menuItemRepository.save(menuItem);
    }
} 