package com.restaurant.orderingsystem.service;

import com.restaurant.orderingsystem.entity.Category;
import com.restaurant.orderingsystem.entity.MenuItem;

import java.util.List;
import java.util.Optional;

/**
 * 菜单项服务接口
 * 提供菜单项的增删改查等功能
 */
public interface MenuItemService {
    
    /**
     * 创建新菜单项
     * @param menuItem 菜单项对象
     * @return 保存后的菜单项
     */
    MenuItem createMenuItem(MenuItem menuItem);
    
    /**
     * 根据ID查询菜单项
     * @param id 菜单项ID
     * @return 菜单项对象
     */
    Optional<MenuItem> getMenuItemById(Long id);
    
    /**
     * 获取所有菜单项
     * @return 菜单项列表
     */
    List<MenuItem> getAllMenuItems();
    
    /**
     * 根据分类查询菜单项
     * @param category 分类对象
     * @return 该分类下的菜单项列表
     */
    List<MenuItem> getMenuItemsByCategory(Category category);
    
    /**
     * 根据分类ID查询菜单项
     * @param categoryId 分类ID
     * @return 该分类下的菜单项列表
     */
    List<MenuItem> getMenuItemsByCategoryId(Long categoryId);
    
    /**
     * 根据菜品名称查询菜单项
     * @param name 菜品名称
     * @return 符合条件的菜单项列表
     */
    List<MenuItem> getMenuItemsByName(String name);
    
    /**
     * 根据可用性查询菜单项
     * @param availability 可用性状态
     * @return 符合条件的菜单项列表
     */
    List<MenuItem> getMenuItemsByAvailability(MenuItem.Availability availability);
    
    /**
     * 更新菜单项
     * @param id 菜单项ID
     * @param menuItemDetails 更新的菜单项详情
     * @return 更新后的菜单项
     */
    MenuItem updateMenuItem(Long id, MenuItem menuItemDetails);
    
    /**
     * 删除菜单项
     * @param id 菜单项ID
     */
    void deleteMenuItem(Long id);
    
    /**
     * 更新菜单项可用性
     * @param id 菜单项ID
     * @param availability 可用性状态
     * @return 更新后的菜单项
     */
    MenuItem updateMenuItemAvailability(Long id, MenuItem.Availability availability);
} 